public class Rutenett {

    // Variables
    int antRader;
    int antKolonner;
    Celle[][] rutene;

    // Constructor
    Rutenett(int antRader, int antKolonner) {
        this.antRader = antRader;
        this.antKolonner = antKolonner;
        this.rutene = new Celle[antRader][antKolonner];
    }

    // Methods
    void lagCelle(int rad, int kol){
        Celle nyCelle = new Celle();   // New cell
        if (Math.random() <= 0.3333){  // If 1/3 chance happened -> Make Alive
            nyCelle.settLevende();
        }
        this.rutene[rad][kol] = nyCelle;  // Return new cell
    }

    void fyllMedTilfeldigeCeller(){
        for (int rad = 0; rad < this.antRader; rad++){
            for (int kol = 0; kol < this.antKolonner; kol++){
                this.lagCelle(rad, kol);  // Repeats the lagCelle for all the grid spaces
            }
        }
    }

    Celle hentCelle(int rad, int kol){
        try{
            return this.rutene[rad][kol];  // Trying to retrieve a cell from the grid
        } catch (Exception ArrayIndexOutOfBoundsException){  // Out of bounds -> None exist -> null
            return null;
        }
    }

    // While researching turns out StringBuilder is a thing for optimization that exists
    // However I did not implement it because I don't think it matters here.
    // That is not the bottleneck for sure. Looking at you kobleAlleCeller >:(

    void tegnRutenett(){
        System.out.println("\n\n\n\n\n\n\n\n\n");  // 10 New lines.
        String bot = "";
        for (int rad = 0; rad < this.antRader; rad++) {  // Repeating for each row -> Create a proper row design
            String top = "+";
            String mid = "|";
            for (int kol = 0; kol < this.antKolonner; kol++) {  // Repeating for each column -> Create a grid design
                top += "---+";
                mid += " " + this.hentCelle(rad, kol).hentStatusTegn() + " |";  // Also taking in cell state
            }
            System.out.println(top);
            System.out.println(mid);
            bot = top;  // bottom looks same as above middle
        }
        System.out.println(bot);
    }

    // Not optimized but too lazy to do better.
    void settNaboer(int rad, int kol){
        Celle celle = this.hentCelle(rad, kol);  // Getting cell
        if (celle == null){                      // Checking if it doesn't exists
            return;                              // If so -> stop method
        }
        for (int x = -1; x < 2; x++){
            for (int y = -1; y < 2; y++){
                if (x == 0 && y == 0){    // It itself can't be it own neighbour
                    continue;             // If we try to -> stop and got to next
                }
                Celle nabo = this.hentCelle(rad - y, kol - x);  // Getting cell
                if (nabo != null){                                      // Checking if cell exists
                    celle.leggTilNabo(nabo);                            // If so -> Add to neighbours
                }
            }
        }
    }

    // Definitely not optimized but too lazy to do better
    void kobleAlleCeller(){
        for (int rad = 0; rad < this.antRader; rad++){
            for (int kol = 0; kol < this.antKolonner; kol++){
                this.settNaboer(rad, kol);  // Setting neighbours for each cell
            }
        }
    }

    // Quite unnecessary
    int antallLevende(){
        int antallLevende = 0;
        for (Celle[] rad : this.rutene){
            for (Celle celle : rad){
                if (celle.erLevende()){  // Checking if cell is alive
                    antallLevende++;     // If so -> Increment by one
                }
            }
        }
        return antallLevende;
    }
}
