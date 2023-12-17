
/**
 * The ClubFullException in BookClubApplication is used to handle situations
    where the book club reaches its maximum capacity and cannot add any more members.
    This custom exception is  thrown in methods like add() in ObjectList/MemberList,
    where there's a check for the club's capacity. If the capacity is exceeded,
    ClubFullException is thrown and then caught in a try-catch block within the same method,
    where an appropriate message is displayed to the user indicating that no more members can be added.
 *
 * @author Tadhg
 * @version 10/12/2023
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

public class BookClubApplication {
    private static final String CLUBFILE = "bookClubState.ser";
    private static final String MEMBER_BOOK_CHOICE_FILE = "memberBookChoices.txt";
    private static final String PAST_BOOKS_FILE = "pastBooks.txt";
    private static final String PAST_MEMBERS_FILE = "pastMembers.txt";
    private static BookClub club = new BookClub();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException, ClubFullException {
        loadClubData(); // Load existing data at the start of the application

        boolean running = true;
        while (running) {
            try {
                showMenu();
                
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        addNewMember();
                        break;
                    case 2:
                        getMembersMonthlyPayment();
                        break;
                    case 3:
                        listAllMembers();
                        break;
                    case 4:
                        findBookChoiceForMember();
                        break;
                    case 5:
                        removeMember();
                        break;
                    case 6:
                        getNumberOfPaymentsMade();
                        break;
                    case 7:
                        getTotalPaymentsMade();
                        break;
                    case 8:
                        markBookAsRead();
                        break;
                    case 9:
                        // Save state
                        saveClubData();
                        running = false;
                        scanner.close();
                        break;
                    default:
                        System.out.println("Invalid option, please try again.");
                }
            } catch (IOException e) {
                System.err.println("An error occurred while reading input: " + e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    // Displays the main menu options to the user.
    private static void showMenu() {
        System.out.println("\n--- Book Club Menu ---");
        System.out.println("1. Add a Member");
        System.out.println("2. Get Members Monthly Payment");
        System.out.println("3. List all Members");
        System.out.println("4. Find a book choice for a particular member");
        System.out.println("5. Remove a Member");
        System.out.println("6. Get number of payments made (per Member)");
        System.out.println("7. Get total payments made (for the entire club)");
        System.out.println("8. Read Book");
        System.out.println("9. Quit");
        System.out.print("Choose an option: ");
    }

    // case 1
    // Adds a new member to the book club.
    private static void addNewMember() throws ClubFullException {
        // Member Info
        Member member = addMemberInputs();
    
        try {
            club.getMembersList().add(member);
    
            // Member added Successfully
            System.out.println("New member added: " + member.getName());
            System.out.println();
            System.out.println("Congratulations " + member.getName() + " you are successfully in our Book club, please now enter your favorite book!");
            System.out.println();
    
            // Book Info
            Book book = addBookInputs();
    
            writeMemberBookChoice(member, book);
            // Book added Successfully
            club.getBookList().add(book);
            System.out.println("You have added: " + book.getTitle() + " to the booklist");
            handlePayment(member);
        } catch (ClubFullException e) {
            System.out.println("Cannot add member: " + e.getMessage());
        }
    }

    // case 2
    // Retrieves and displays monthly payment details for a specific member.
    private static void getMembersMonthlyPayment() throws ClubFullException {
        MemberList members = club.getMembersList();
        printMemberList(members);
        
        Member selectedMember = selectMember(members);
        if (selectedMember != null) {
            processPaymentOptions(selectedMember);
        } else {
            System.out.println("Invalid number. Please select a valid number.");
        }
    }

    // case 3
    // Lists all members and their associated books.
    private static void listAllMembers() {
        MemberList members = club.getMembersList();
        BookList books = club.getBookList();
        if (members.getTotal() == 0) {
            System.out.println("There are no members in the book club.");
        } else {
            System.out.println("List of Members in the Book Club:");
            System.out.println("----------------------------------------------------------------------------------------------------");
            System.out.printf("%-3s %-20s %-20s %-15s %-10s %-10s\n", "#", "Name", "Address", "Phone", "Paid", "Total €");
            System.out.println("----------------------------------------------------------------------------------------------------");
    
            for (int i = 0; i < members.getTotal(); i++) {
                Member member = members.search(i);
                String paidStatus = member.getPaid() ? "Yes" : "No";
    
                System.out.printf("%-3d %-20s %-20s %-15s %-10s %-10.2f\n",
                                  i + 1,
                                  member.getName(),
                                  member.getAddress(),
                                  member.getPhone(),
                                  paidStatus,
                                  member.getPayment().calculateTotalPaid());
            }
            System.out.println("----------------------------------------------------------------------------------------------------");
            delay();
        }

        System.out.println("List of Books in the Book Club:");
            System.out.println("--------------------------------------------------------------------------");
            System.out.printf("%-3s %-20s %-20s %-20s\n", "#", "Book Title", "Genre", "Author");
            System.out.println("--------------------------------------------------------------------------");

            for (int i = 0; i < books.getTotal(); i++) {
                Book book = books.getBook(i);
                if (book != null) { // Check if book is not null
                    System.out.printf("%-3d %-20s %-20s %-20s\n",
                        i + 1,
                        book.getTitle(),
                        book.getGenre(),
                        book.getWriter());
                } else {
                    books.remove(i);
                }
            }
            System.out.println("---------------------------------------------------------------------------");
            delay();
        }

    // case 4
    // Finds and displays the book choice for a specific member.
    private static void findBookChoiceForMember() throws IOException {
        MemberList members = club.getMembersList();
        printMemberList(members);
        Member selectedMember = selectMember(members);
        try (Scanner fileScanner = new Scanner(new File(MEMBER_BOOK_CHOICE_FILE))) {
            while (fileScanner.hasNextLine()) {
                String[] line = fileScanner.nextLine().split(";");
                if (line[0].equals(selectedMember.getName())) {
                    System.out.println("Book choice for " + selectedMember.getName() + ": " + line[1]);
                    return;
                }
            }
            System.out.println("No book choice recorded for " + selectedMember.getName());
        } catch (IOException e) {
            System.err.println("Error occurred while reading the member-book choice file: " + e.getMessage());
        }
        delay();
    }

    // case 5
    // Removes a member from the book club.
    private static void removeMember() throws IOException {
        MemberList members = club.getMembersList();
        BookList books = club.getBookList();
        printMemberList(members);
        Member selectedMember = selectMember(members);
    
        if (club.deleteMember(selectedMember)) {
            // Remove the member's book choice and get the book object
            Book bookToRemove = removeMemberBookChoiceAndGetBook(selectedMember, books);
    
            if (bookToRemove != null) {
                // Remove the book from the book list
                int bookIndex = books.indexOf(bookToRemove);
                if (bookIndex != -1) {
                    books.remove(bookIndex);
    
                    // Save the book to the past books file
                    saveBookToFile(bookToRemove, PAST_BOOKS_FILE);
                }
            }
    
            // Check if the member has paid in full and process a refund if necessary
            if (selectedMember.getPaid()) {
                System.out.println("Member has paid in full. Processing refund...");
            }
    
            // Save member to past members file
            saveMemberToFile(selectedMember, PAST_MEMBERS_FILE);
            System.out.println("Member: " + selectedMember.getName() + " removed successfully.");
        } else {
            System.out.println("Member with the name '" + selectedMember.getName() + "' not found.");
        }
        delay();
    }

    // case 6
    // Displays the number of payments made by each member.
    private static void getNumberOfPaymentsMade() throws IOException {
        MemberList members = club.getMembersList();
        System.out.println();
        System.out.println("Number of payments made by each member:");
        for (int i = 0; i < members.getTotal(); i++) {
            Member member = (Member) members.getObject(i);
            int paymentsMade = member.getPayment().getTotal();
            if (paymentsMade == 1) {
                System.out.println(member.getName() + " has made " + paymentsMade + " payment.");
            } else {
                System.out.println(member.getName() + " has made " + paymentsMade + " payments.");
            }
        }
        delay();
    }

    // case 7
    // Calculates and displays the total payments made by all members.
    private static void getTotalPaymentsMade() {
        double totalPayments = 0;
        MemberList members = club.getMembersList();
        for (int i = 0; i < members.getTotal(); i++) {
            Member member = (Member) members.getObject(i);
            PaymentList payments = member.getPayment();
            for (int j = 0; j < payments.getTotal(); j++) {
                Payment payment = payments.getPayment(j);
                totalPayments += payment.getAmount();
            }
        }
        System.out.printf("The total payments made for the entire club is: €%.2f\n", totalPayments);
    }

    // case 8 
    // Reads the first book in book list and adds in to pastBooks.txt.
    private static void markBookAsRead() throws IOException {
        BookList books = club.getBookList();
        if (books.getTotal() == 0) {
            System.out.println("No books available to mark as read.");
            return;
        }
    
        Book bookToMarkAsRead = books.getBook(0);
    
        System.out.println("Marking the book as read: " + bookToMarkAsRead.getTitle());
    
        books.remove(0);
    
        saveBookToFile(bookToMarkAsRead, PAST_BOOKS_FILE);
    
        System.out.println("Book marked as read and moved to past books: " + bookToMarkAsRead.getTitle());
    }
    

    // helper methods - files
    // Saves the current state of the book club to a file
    private static void saveClubData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(CLUBFILE))) {
            oos.writeObject(club);
            System.out.println("BookClub state has been saved to " + CLUBFILE);
        } catch (IOException e) {
            System.out.println("Error occurred while saving BookClub state: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Loads the book club state from a file
    private static void loadClubData() {
        File file = new File(CLUBFILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(CLUBFILE))) {
                club = (BookClub) ois.readObject();
                System.out.println("BookClub state has been loaded from " + CLUBFILE);
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error occurred while loading BookClub state: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            club = new BookClub();
        }
    }

    // Writes a member's details to a file.
    private static void saveMemberToFile(Member member, String filename) {
        try (FileWriter fw = new FileWriter(filename, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw)) {
            
            // Write member details to file
            out.println(member.getName() + "," + member.getAddress() + "," + member.getPhone());
            
        } catch (IOException e) {
            System.err.println("An error occurred while writing to the past members file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Writes a book's details to a file
    private static void saveBookToFile(Book book, String filename) {
        try (FileWriter fw = new FileWriter(filename, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
    
            out.println(book.getTitle() + ";" + book.getGenre() + ";" + book.getWriter());
            
        } catch (IOException e) {
            System.err.println("Error occurred while writing to the past books file: " + e.getMessage());
        }
    }

    // Removes a member's book choice and retrieves the book.
    private static Book removeMemberBookChoiceAndGetBook(Member member, BookList books) throws IOException {
        File inputFile = new File(MEMBER_BOOK_CHOICE_FILE);
        File tempFile = new File("tempMemberBookChoices.txt");
        Book bookToRemove = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                String[] lineParts = currentLine.split(";");
                if (lineParts[0].equals(member.getName())) {
                    // Get the book to be removed
                    bookToRemove = getBookByTitle(books, lineParts[1]);
                } else {
                    writer.write(currentLine + System.lineSeparator());
                }
            }
        }

        // Replace the original file
        replaceFile(inputFile, tempFile);

        return bookToRemove;
    }

    private static Book getBookByTitle(BookList books, String title) {
        for (int i = 0; i < books.getTotal(); i++) {
            Book book = (Book) books.getObject(i);
            if (book.getTitle().equals(title)) {
                return book;
            }
        }
        return null;
    }
    
    private static void replaceFile(File originalFile, File tempFile) throws IOException {
        if (!originalFile.delete()) {
            System.out.println("Could not delete the original file");
        } else if (!tempFile.renameTo(originalFile)) {
            System.out.println("Could not rename the temp file to the original file name");
        }
    }

    // Records a member's book choice in a file.
    private static void writeMemberBookChoice(Member member, Book book) {
        try (FileWriter fw = new FileWriter(MEMBER_BOOK_CHOICE_FILE, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
    
            out.println(member.getName() + ";" + book.getTitle());
            
        } catch (IOException e) {
            System.err.println("Error occurred while writing to the member-book choice file: " + e.getMessage());
        }
    }

    // helper methods - extra
    // Collects and returns new member details from user input.
    private static Member addMemberInputs() {
        String name = "";
        while (name.isEmpty()) {
            System.out.println("Enter member's name:");
            name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                System.out.println("Member's name cannot be empty. Please enter a valid name.");
            }
        }
    
        String address = "";
        while (address.isEmpty()) {
            System.out.println("Enter member's address:");
            address = scanner.nextLine().trim();
            if (address.isEmpty()) {
                System.out.println("Member's address cannot be empty. Please enter a valid address.");
            }
        }
    
        String phone = "";
        while (phone.isEmpty() || !phone.matches("\\d{10}")) {
            System.out.println("Enter member's phone (10 digits):");
            phone = scanner.nextLine().trim();
            if (phone.isEmpty() || !phone.matches("\\d{10}")) {
                System.out.println("Invalid phone number. Please enter a 10 digit phone number.");
            }
        }
        Member member = new Member(name, address, phone);
        return member;
    }

    // Collects and returns new book details from user input.
    private static Book addBookInputs() {
        String title = "";
        while (title.isEmpty()) {
            System.out.println("Enter book's title:");
            title = scanner.nextLine().trim();
            if (title.isEmpty()) {
                System.out.println("Book title cannot be empty.");
            }
        }
    
        String genre = "";
        while (genre.isEmpty()) {
            System.out.println("Enter book's genre:");
            genre = scanner.nextLine().trim();
            if (genre.isEmpty()) {
                System.out.println("Book genre cannot be empty.");
            }
        }
    
        String writer = "";
        while (writer.isEmpty()) {
            System.out.println("Enter book's writer:");
            writer = scanner.nextLine().trim();
            if (writer.isEmpty()) {
                System.out.println("Book writer cannot be empty.");
            }
        }
    
        Book book = new Book(title, genre, writer);
        return book;
    }

    // Manages the payment process for a member.
    private static void handlePayment(Member member) throws ClubFullException {
        boolean paymentQuit = false;
        while (!paymentQuit) {
            System.out.println("You have " + (60 - member.getPayment().calculateTotalPaid()) + "£ left to pay for Member fees");
            System.out.println("Select an option by entering the corresponding number:");
            System.out.println("1. Make Single Member Payment \n2. Make Full Member Payment");
            System.out.print("Enter a number: ");
    
            int choice = scanner.nextInt();
            scanner.nextLine();
            System.out.println();
    
            switch (choice) {
                case 1:
                    handleSingleMemberPayment(member);
                    paymentQuit = true;
                    break;
                case 2:
                    makeFullPayment(member);
                    System.out.println();
                    System.out.print("You have paid for full member fee, you have no more payments to make");
                    paymentQuit = true;
                    break;
                default:
                    System.out.println("Invalid option, please try again.");
            }
        }
    }

    //  Processes a single payment for a member.
    private static void handleSingleMemberPayment(Member member) throws ClubFullException {
        boolean singlePayment = false;
        while (!singlePayment) {
            int amount = 5;
            String month;

            String[] months = new String[]{"January", "February", "March", "April", "May", "June", 
                                       "July", "August", "September", "October", "November", "December"};
            while (true) {
                System.out.println("Select a month:");
                for (int i = 0; i < months.length; i++) {
                    System.out.println((i + 1) + ". " + months[i]);
                }
                System.out.print("Enter the number of the month: ");
                int selectedNumber = scanner.nextInt();
                scanner.nextLine();
                System.out.println();
        
                if (selectedNumber >= 1 && selectedNumber <= 12) {
                    month = months[selectedNumber - 1];
                    break;
                }
            }
    
            Payment payment = new Payment(month, amount);
            if (!member.getPaid()) {
                if (member.makePayment(payment)) {
                    if (member.getPayment().calculateTotalPaid() == 60) {
                        System.out.println();
                        System.out.print("You have paid for the full member fee, you have no more payments to make");
                    } else {
                        System.out.println();
                        System.out.print("You have paid " + amount + "£ on " + month + ", you have " + (60 - member.getPayment().calculateTotalPaid()) + "£ left to pay");
                    }
                    singlePayment = true;
                }
            } else {
                System.out.println("You have already paid for the full member fee, you have no more payments to make");
                System.out.println();
                return;
            }
        }
    }

    //  Processes a full payment for a member
    private static void makeFullPayment(Member member) throws ClubFullException {
        double fullPayment = 60.0;
        Payment payment = new Payment( "Full Year", fullPayment);
        member.makePayment(payment);
    }
       
    private static void printMemberList(MemberList members) {
        System.out.println("Select a member by entering the corresponding number:");
        for (int i = 0; i < members.getTotal(); i++) {
            System.out.println((i + 1) + ". " + members.search(i).getName());
        }
    }
    
    // Selects a member based on user input
    private static Member selectMember(MemberList members) {
        System.out.print("Enter a number: ");
        int selectedNumber = scanner.nextInt();
        scanner.nextLine();
        System.out.println();
    
        if (selectedNumber >= 1 && selectedNumber <= members.getTotal()) {
            return members.search(selectedNumber - 1);
        }
        return null;
    }

    // Provides payment options for a selected member
    private static void processPaymentOptions(Member selectedMember) throws ClubFullException {
        boolean paymentListQuit = false;
        while (!paymentListQuit) {
            System.out.println("Select an option by entering the corresponding number:");
            System.out.println("1. Print Member Payments \n2. Make A Member Payment \n3. Exit To Menu");
            System.out.print("Enter a number: ");
    
            int choice = scanner.nextInt();
            scanner.nextLine();
    
            switch (choice) {
                case 1:
                    printMemberPayments(selectedMember);
                    break;
                case 2:
                    if (selectedMember.getPaid()) {
                        System.out.println("You have already paid for the full member fee, you have no more payments to make");
                        System.out.println();
                        break;
                    }
                    else {
                        handleSingleMemberPayment(selectedMember);
                        break;
                    }
                case 3:
                    paymentListQuit = true;
                    break;
                default:
                    System.out.println("Invalid option, please try again.");
            }
        }
    }

    //  Prints all payment details for a selected member.
    private static void printMemberPayments(Member selectedMember) {
        PaymentList payments = selectedMember.getPayment();
        if (!payments.isEmpty()) {
            System.out.println("Payments for " + selectedMember.getName() + ":");
            for (int i = 0; i < payments.getTotal(); i++) {
                Payment payment = payments.getPayment(i);
                System.out.println(payment.toString());
            }
            delay();
        } else {
            System.out.println(selectedMember.getName() + " has not made any payments.");
        }
    }
    
    // Pauses execution to allow the user to read output before proceeding.
    private static void delay() {
        System.out.println("\n\nPress Enter to continue ...");
        scanner.nextLine();
    }
}
