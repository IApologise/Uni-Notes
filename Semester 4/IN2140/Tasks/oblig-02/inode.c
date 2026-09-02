#include "inode.h"
#include "block_allocation.h"

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <errno.h>

#define EXTENTSIZE 4

// Naive way to keep track of the IDs.
static uint32_t highest_id = -1;  // This value is saved in and loaded from the master_file_table
uint32_t assign_id() {return ++highest_id;}

struct inode* create_file( struct inode* parent, const char* name, char readonly, int size_in_bytes )
{
    // Validating inputs
    if (parent == NULL || name == NULL) return NULL;
    if (!parent->is_directory) return NULL;
    if (find_inode_by_name(parent, name) != NULL) return NULL;

    // Creating new file
    struct inode* file = malloc(sizeof(struct inode));
    // Assigning ID later to prevent ID assignment rollbacks
    file->name = strdup(name);
    file->is_directory = 0;
    file->is_readonly = readonly;
    file->filesize = size_in_bytes;

    // Allocating entries: Pointers to extents
    uint32_t extents = (uint32_t) (((uint64_t) file->filesize - 1) / BLOCKSIZE + 1);
    file->num_entries = (uint32_t) (((uint64_t) extents - 1) / 4 + 1);
    file->entries = malloc(sizeof(struct Extent) * file->num_entries);

    // Allocating blocks to hold file data
    for (int i = 0; i < file->num_entries; i++) {
        struct Extent* entry = (struct Extent*) &file->entries[i];
        int bta = extents > EXTENTSIZE ? EXTENTSIZE : (int) extents;
        entry->blockno = (uint32_t) allocate_blocks(bta);
        entry->extent = (uint32_t) bta;
        extents -= (uint32_t) bta;

        // Validating block allocation
        if (entry->blockno == -1) {
            free(file->name);
            while (--i >= 0) {
                entry = (struct Extent*) &file->entries[i];
                for (int j = (int) entry->blockno; j < entry->blockno + entry->extent; j++) free_block(j);
            } free(file->entries);
            free(file);
            return NULL;
        }
    }  // Assigning file to parent
    uintptr_t* mo_entries = realloc(parent->entries, (parent->num_entries + 1) * sizeof(uintptr_t));
    if (mo_entries == NULL) {  // Error handling
        free(file->name);
        for (int i = 0; i < file->num_entries; i++) {
            struct Extent* entry = (struct Extent*) &file->entries[i];
            for (int j = (int) entry->blockno; j < entry->blockno + entry->extent; j++) free_block(j);
        } free(file->entries);
        free(file);
        return NULL;
    } parent->entries = mo_entries;
    parent->entries[parent->num_entries++] = (uintptr_t) file;

    // Success -> Assign ID & return pointer to file
    file->id = assign_id();
    return file;
}

struct inode* create_dir( struct inode* parent, const char* name )
{
    // Validating inputs
    if (name == NULL) return NULL;
    if (parent != NULL) {
        if (find_inode_by_name(parent, name) != NULL) return NULL;
    }

    // Creating new directory
    struct inode* new_dir = malloc(sizeof(struct inode));
    // Assigning ID later to prevent ID assignment rollbacks
    new_dir->name = strdup(name);
    new_dir->is_directory = 1;
    new_dir->is_readonly = 0;
    new_dir->filesize = 0;

    // Allocating entries: Pointers to inodes
    new_dir->num_entries = 0;
    new_dir->entries = NULL;

    // Assigning directory to parent
    if (parent != NULL) {
        if (parent->is_directory) {
            uintptr_t* mo_entries = realloc(parent->entries, (parent->num_entries + 1) * sizeof(uintptr_t));
            if (mo_entries == NULL) {  // Error handling
                free(new_dir->name);
                free(new_dir->entries);
                free(new_dir);
                return NULL;
            } parent->entries = mo_entries;
            parent->entries[parent->num_entries++] = (uintptr_t) new_dir;
        }
    }  // Success -> Assign ID & return pointer to directory
    new_dir->id = assign_id();
    return new_dir;
}

struct inode* find_inode_by_name( struct inode* parent, const char* name )
{
    // Validating inputs
    if (parent == NULL || name == NULL) return NULL;
    if (!parent->is_directory) return NULL;

    // Searching for inode
    for (uint32_t i = 0; i < parent->num_entries; i++) {
        struct inode* child = (struct inode*) parent->entries[i];
        if (strcmp(child->name, name) == 0) return child;
    }
    // Inode not found
    return NULL;
}

int delete_file( struct inode* parent, struct inode* node )
{
    // Validating input
    if (node == NULL || parent == NULL) return -1;
    if (!parent->is_directory || node->is_directory) return -1;

    // Locating file in the given parent directory
    uint32_t i = 0;
    while (i < parent->num_entries && (struct inode*) parent->entries[i] != node) i++;
    if (i >= parent->num_entries) return -1;  // Node not found -> Error

    // Deleting parent's reference to the file
    while (++i < parent->num_entries) {
        parent->entries[i - 1] = parent->entries[i];
    } parent->num_entries--;
    if (parent->num_entries == 0) {
        free(parent->entries);
        parent->entries = NULL;
    } else {
        uintptr_t* pointer = realloc(parent->entries, parent->num_entries * sizeof(uintptr_t));
        if (pointer != NULL) parent->entries = pointer;
    }
    // Deleting the file
    free(node->name);
    for (int j = 0; j < node->num_entries; j++) {
        struct Extent* entry = (struct Extent*) &node->entries[j];
        for (int k = (int) entry->blockno; k < entry->blockno + entry->extent; k++) {
            free_block(k);
        }
    } free(node->entries);
    free(node);

    // Great success :D
    return 0;
}

int delete_dir( struct inode* parent, struct inode* node )
{
    // Validating input
    if (node == NULL || parent == NULL) return -1;
    if (!parent->is_directory || !node->is_directory) return -1;
    if (node->num_entries > 0) return -1;

    // Locating node directory in the given parent directory
    uint32_t i = 0;
    while (i < parent->num_entries && (struct inode*) parent->entries[i] != node) i++;
    if (i >= parent->num_entries) return -1;  // Node not found -> Error

    // Deleting parent's reference to child
    while (++i < parent->num_entries) {
        parent->entries[i - 1] = parent->entries[i];
    } parent->num_entries--;
    if (parent->num_entries == 0) {
        free(parent->entries);
        parent->entries = NULL;
    } else {
        uintptr_t* pointer = realloc(parent->entries, parent->num_entries * sizeof(uintptr_t));
        if (pointer != NULL) parent->entries = pointer;
    }
    // Deleting the child directory
    free(node->name);
    free(node->entries);
    free(node);

    // Great success :D
    return 0;
}

void save_inodes( const char* master_file_table, struct inode* root )
{
    // Creating master file table
    FILE* mft = fopen(master_file_table, "wb");

    // Setting up the cascade
    struct inode** inodes = malloc(sizeof(struct inode*));
    inodes[0] = root;
    uint32_t i_len = 1;

    // Saving inodes...
    for (int i = 0; i < i_len; i++) {
        struct inode* this = inodes[i];

        // Writing data to file: First we got id
        fwrite(&this->id, sizeof(uint32_t), 1, mft);

        // Then the size of its name and the name itself
        uint32_t n_len = strlen(this->name) + 1;
        fwrite(&n_len, sizeof(uint32_t), 1, mft);
        fwrite(this->name, sizeof(char), n_len, mft);

        // Next some flags
        fwrite(&this->is_directory, sizeof(char), 1, mft);
        fwrite(&this->is_readonly, sizeof(char), 1, mft);

        // If it is a directory we save its entries
        if (this->is_directory) {

            // Saving & updating inodes length
            fwrite(&this->num_entries, sizeof(uint32_t), 1, mft);
            struct inode** mo_inodes = realloc(inodes, sizeof(struct inode*) * (i_len + this->num_entries));
            if (mo_inodes == NULL) {  // Error handling
                fclose(mft);
                free(inodes);
                return;
            } inodes = mo_inodes;

            // Saving entry IDs
            for (int j = 0; j < this->num_entries; j++) {
                struct inode* child = (struct inode*) this->entries[j];
                uint64_t childID = child->id;
                fwrite(&childID, sizeof(uint64_t), 1, mft);

                // Including children (Breadth-First Search)
                inodes[i_len++] = child;
            }
        }
        else {  // If it is a file we save its size and its Extent
            fwrite(&this->filesize, sizeof(uint32_t), 1, mft);
            fwrite(&this->num_entries, sizeof(uint32_t), 1, mft);
            for (int j = 0; j < this->num_entries; j++) {
                struct Extent* entry = (struct Extent*) &this->entries[j];
                fwrite(&entry->blockno, sizeof(uint32_t), 1, mft);
                fwrite(&entry->extent, sizeof(uint32_t), 1, mft);
            }
        }
    } // Cleanup
    fclose(mft);
    free(inodes);
}

// RIP BFS loading approach, and hello the intended loading approach, i.e. where we do not
// assume some concrete serialization approach like the commented one above (it assumed BFS)
struct inode* load_inodes( const char* master_file_table )
{
    // Opening master file table
    FILE* mft = fopen(master_file_table, "rb");
    if (!mft) return NULL;

    // Small setup
    struct inode** inodes = NULL;
    int in_len = 0;

    // Reading...
    struct inode* this = malloc(sizeof(struct inode));
    while (fread(&this->id, sizeof(uint32_t), 1, mft) == 1) {

        // Updating next_id value (highest is kept)
        if (this->id > highest_id) highest_id = this->id;

        // Reading and loading general inode data
        uint32_t n_len;
        fread(&n_len, sizeof(uint32_t), 1, mft);
        this->name = malloc(n_len * sizeof(char));
        fread(this->name, sizeof(char), n_len, mft);
        fread(&this->is_directory, sizeof(char), 1, mft);
        fread(&this->is_readonly, sizeof(char), 1, mft);

        // Reading data for directories and files is different
        if (this->is_directory) {  // Reading as directory data
            this->filesize = 0;
            fread(&this->num_entries, sizeof(uint32_t), 1, mft);
            if (this->num_entries > 0) {  // Edge case handling
                this->entries = malloc(sizeof(uintptr_t) * this->num_entries);
            } else { this->entries = NULL; }

            // Entries will have children IDs, for now...
            for (int entry = 0; entry < this->num_entries; entry++) {
                uint64_t id;
                fread(&id, sizeof(uint64_t), 1, mft);
                this->entries[entry] = id;
            }
        } else {  // Reading as file data
            fread(&this->filesize, sizeof(uint32_t), 1, mft);
            fread(&this->num_entries, sizeof(uint32_t), 1, mft);
            if (this->num_entries > 0) {  // Edge casse handling
                this->entries = malloc(sizeof(struct Extent) * this->num_entries);
            } else { this->entries = NULL; }
            for (int i = 0; i < this->num_entries; i++) {
                struct Extent* entry = (struct Extent*) &this->entries[i];
                fread(&entry->blockno, sizeof(uint32_t), 1, mft);
                fread(&entry->extent, sizeof(uint32_t), 1, mft);
            }
        }  // Allocating more space to inodes array to house this one
        struct inode** mo_inodes = realloc(inodes, sizeof(struct inode*) * ++in_len);
        if (mo_inodes == NULL) {  // Error handling
            fclose(mft);
            in_len--;  // Roll back count increment
            for (int inode = 0; inode < in_len; inode++) {
                free(inodes[inode]->name);
                free(inodes[inode]->entries);
                free(inodes[inode]);
            } free(inodes);
            free(this);
            return NULL;
        } inodes = mo_inodes;
        inodes[in_len - 1] = this;  // Registering this inode

        // Preparing to load next directory/file
        this = malloc(sizeof(struct inode));
    } fclose(mft);  // Done reading -> Close file
    free(this);     // Also free unused memory

    // Locating root
    if (in_len < 1) {  // Error handling
        free(inodes);
        return NULL;
    }  struct inode* root = inodes[0];

    // All inodes registered, replacing IDs with pointers
    for (int parent = 0; parent < in_len; parent++) {
        this = inodes[parent];

        if (this->is_directory) {  // Only directories can have children
            for (int entry = 0; entry < this->num_entries; entry++) {
                char NNFE = 1;  // Stands for "Node Not Found Error"
                for (int child = 0 ; child < in_len; child++) {  // Locating child...

                    // ID found -> Replace it with a pointer
                    if (inodes[child]->id == (uint64_t) this->entries[entry]) {
                        this->entries[entry] = (uintptr_t) inodes[child];
                        NNFE = 0;
                        break;
                    }
                }  if (NNFE) {  // Missing node -> Abort
                    for (int i = 0; i < in_len; i++) {
                        free(inodes[i]->name);
                        free(inodes[i]->entries);
                        free(inodes[i]);
                    } free(inodes);
                    return NULL;
                }
            }
        }
    }  // Success -> Free up unused memory
    free(inodes);
    return root;  // And return pointer to root node
}

void fs_shutdown( struct inode* inode )
{
    // Validating input
    if (inode == NULL) return;

    // Setting up the shutdown
    struct inode** inodes = malloc(sizeof(struct inode*));
    inodes[0] = inode;
    uint32_t i_len = 1;

    // Getting children (Breadth-First Search, i.e. cascade)
    for (int i = 0; i < i_len; i++) {
        struct inode* next = inodes[i];
        if (next->is_directory) {
            struct inode** mo_inodes = realloc(inodes, sizeof(struct inode*) * (i_len + next->num_entries));
            if (mo_inodes == NULL) {
                free(inodes);
                return;
            } inodes = mo_inodes;
            for (int j = 0; j < next->num_entries; j++) {
                inodes[i_len++] = (struct inode*) next->entries[j];
            }
        }
    } // Freeing inode and its children
    while (i_len > 0) {
        struct inode* next = inodes[--i_len];
        free(next->name);
        free(next->entries);
        free(next);
    } free(inodes);
}

/* This static variable is used to change the indentation while debug_fs
 * is walking through the tree of inodes and prints information.
 */
static int indent = 0;

static void debug_fs_print_table( const char* table );
static void debug_fs_tree_walk( struct inode* node, char* table );

void debug_fs( struct inode* node )
{
    char* table = calloc( NUM_BLOCKS, 1 );
    debug_fs_tree_walk( node, table );
    debug_fs_print_table( table );
    free( table );
}

static void debug_fs_tree_walk( struct inode* node, char* table )
{
    if( node == NULL ) {
        printf("node is empty!");
        return;
    };
    for( int i=0; i<indent; i++ )
        printf("  ");
    if( node->is_directory )
    {
        printf("%s (id %d)\n", node->name, node->id );
        indent++;
        for( int i=0; i<node->num_entries; i++ )
        {
            struct inode* child = (struct inode*)node->entries[i];
            debug_fs_tree_walk( child, table );
        }
        indent--;
    }
    else
    {
        printf("%s (id %d size %d)\n", node->name, node->id, node->filesize );

        /* The following is an ugly solution. We expect you to discover a
         * better way of handling extents in the node->entries array, and did
         * it like this because we don't want to give away a good solution here.
         */
        uint32_t* extents = (uint32_t*)node->entries;

        for( int i=0; i<node->num_entries; i++ )
        {
            for( int j=0; j<extents[2*i+1]; j++ )
            {
                table[ extents[2*i]+j ] = 1;
            }
        }
    }
}

static void debug_fs_print_table( const char* table )
{
    printf("Blocks recorded in master file table:");
    for( int i=0; i<NUM_BLOCKS; i++ )
    {
        if( i % 20 == 0 ) printf("\n%03d: ", i);
        printf("%d", table[i] );
    }
    printf("\n\n");
}

