package GUI;

public class Book extends Item {
    public static int getNextId() {
        return nextId;
    }

    public static void setNextId(int nextId) {
        Book.nextId = nextId;
    }

    static int nextId;
    String author;
    int year;

    //int popularityCount;

    Book(int iT, String t, String a, int y, int pC, double p$){

        itemType = iT;
        id = nextId++;
        title = t;
        author = a;
        year = y;
        popularityCount = pC;
        itemPrice = p$;

        System.out.println("New Book Added: ID: "+id+" Title: "+title+" Author: "+author+" Year: "+year);
    }

    public Book(int iT, int i, String t, String a, int y, int pC, int p$){

        itemType = iT;
        id = i;
        title = t;
        author = a;
        year = y;
        popularityCount = pC;
        itemPrice = p$;

        System.out.print("New Book Added: ");
    }

    public void displayDetails(){
        System.out.println("ID: "+ id + " Title: " + title + " by Author: " + author + "(" + year + ") with popularity: " + popularityCount + " priced at : $"+ itemPrice);
    }

    @Override
    public double calculatePrice() {
        return (itemPrice + (itemPrice*20)/100 + 200);
    }

    public String getName(){
        return title;
    }

    public String getAuthor(){
        return author;
    }

    public int getPublicationYear(){
        return year;
    }

    public String getReadItem(){
        return "HIIII";
    }

    public int getPopularityCount(){
        return popularityCount;
    }





}
