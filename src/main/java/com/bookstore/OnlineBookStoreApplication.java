package com.bookstore;

import com.bookstore.entity.Book;
import com.bookstore.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.math.BigDecimal;

@SpringBootApplication
public class OnlineBookStoreApplication implements CommandLineRunner {

    @Autowired
    private BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(OnlineBookStoreApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // Add sample books if the database is empty
        if (bookService.getAllBooks().isEmpty()) {
            addSampleBooks();
        }
    }

    private void addSampleBooks() {
        // Enhanced sample books data with more variety
        Book[] sampleBooks = {
            // Fantasy
            new Book("The Great Gatsby", "F. Scott Fitzgerald", 
                    "A classic novel of the Jazz Age, exploring themes of idealism, resistance to change, social upheaval, and excess.", 
                    "978-0-7432-7356-5", new BigDecimal("12.99"), 50),
            
            new Book("To Kill a Mockingbird", "Harper Lee", 
                    "A gripping, heart-wrenching tale of race and class, innocence and injustice, hatred and love, and the power of integrity.", 
                    "978-0-06-112008-4", new BigDecimal("14.99"), 30),
            
            new Book("1984", "George Orwell", 
                    "A dystopian social science fiction novel that examines the consequences of totalitarianism, mass surveillance, and repressive regimentation.", 
                    "978-0-452-28423-4", new BigDecimal("11.99"), 25),
            
            new Book("Pride and Prejudice", "Jane Austen", 
                    "A romantic novel of manners that depicts the emotional development of protagonist Elizabeth Bennet.", 
                    "978-0-14-143951-8", new BigDecimal("9.99"), 40),
            
            new Book("The Hobbit", "J.R.R. Tolkien", 
                    "A fantasy novel about the adventures of hobbit Bilbo Baggins, who is hired as a burglar by a group of dwarves.", 
                    "978-0-547-92822-7", new BigDecimal("15.99"), 35),
            
            new Book("Harry Potter and the Sorcerer's Stone", "J.K. Rowling", 
                    "The first novel in the Harry Potter series, following Harry Potter's first year at Hogwarts School of Witchcraft and Wizardry.", 
                    "978-0-590-35340-3", new BigDecimal("18.99"), 60),
            
            // Science Fiction
            new Book("Dune", "Frank Herbert", 
                    "A science fiction novel set in the distant future amidst a feudal interstellar society.", 
                    "978-0-441-17271-9", new BigDecimal("16.99"), 20),
            
            new Book("Foundation", "Isaac Asimov", 
                    "The first novel in Isaac Asimov's Foundation series, a epic saga of the Galactic Empire.", 
                    "978-0-553-29335-7", new BigDecimal("13.99"), 15),
            
            // Mystery
            new Book("The Da Vinci Code", "Dan Brown", 
                    "A mystery thriller novel that follows symbologist Robert Langdon as he investigates a murder in the Louvre Museum.", 
                    "978-0-307-27767-8", new BigDecimal("12.99"), 45),
            
            // Romance
            new Book("The Notebook", "Nicholas Sparks", 
                    "A romantic drama about a poor young man who falls in love with a rich young woman.", 
                    "978-0-446-60523-8", new BigDecimal("10.99"), 55),
            
            // History
            new Book("Sapiens: A Brief History of Humankind", "Yuval Noah Harari", 
                    "A book exploring the history of humankind from the evolution of archaic human species in the Stone Age.", 
                    "978-0-06-231609-7", new BigDecimal("19.99"), 25),
            
            // Education
            new Book("Atomic Habits", "James Clear", 
                    "A guide to building good habits and breaking bad ones with proven strategies.", 
                    "978-0-7352-1129-2", new BigDecimal("17.99"), 40)
        };

        // Set categories for sample books
        sampleBooks[0].setCategory("Classic Literature");
        sampleBooks[1].setCategory("Classic Literature");
        sampleBooks[2].setCategory("Science Fiction");
        sampleBooks[3].setCategory("Romance");
        sampleBooks[4].setCategory("Fantasy");
        sampleBooks[5].setCategory("Fantasy");
        sampleBooks[6].setCategory("Science Fiction");
        sampleBooks[7].setCategory("Science Fiction");
        sampleBooks[8].setCategory("Mystery");
        sampleBooks[9].setCategory("Romance");
        sampleBooks[10].setCategory("History");
        sampleBooks[11].setCategory("Education");

        // Save sample books
        for (Book book : sampleBooks) {
            bookService.saveBook(book);
        }

        System.out.println("🎉 Sample books added to the database!");
        System.out.println("📚 Total books: " + sampleBooks.length);
        System.out.println("🚀 Online Book Store is ready to launch!");
    }
}
