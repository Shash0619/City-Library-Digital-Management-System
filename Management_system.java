import java.io.*;
import java.util.*;

class BookInfo {
    int bookId;
    String bookTitle, bookAuthor;
    boolean issuedStatus;

    BookInfo(int bookId, String bookTitle, String bookAuthor) {
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
        this.issuedStatus = false;
    }

    String toData() {
        return bookId + "," + bookTitle + "," + bookAuthor + "," + issuedStatus;
    }
}

class MemberInfo {
    int memId;
    String memName;
    List<Integer> takenBooks = new ArrayList<>();

    MemberInfo(int memId, String memName) {
        this.memId = memId;
        this.memName = memName;
    }

    String toData() {
        return memId + "," + memName + "," + takenBooks.toString();
    }
}

class LibrarySystem {
    Map<Integer, BookInfo> bookMap = new HashMap<>();
    Map<Integer, MemberInfo> memberMap = new HashMap<>();

    String bookFile = "library_books.txt";
    String memFile = "library_members.txt";

    LibrarySystem() {
        loadRecords();
    }

    void addNewBook(int id, String t, String a) {
        bookMap.put(id, new BookInfo(id, t, a));
        saveRecords();
        System.out.println("New book added.");
    }

    void registerMember(int id, String name) {
        memberMap.put(id, new MemberInfo(id, name));
        saveRecords();
        System.out.println("New member registered.");
    }

    void giveBook(int mid, int bid) {
        if (!bookMap.containsKey(bid) || !memberMap.containsKey(mid)) {
            System.out.println("Invalid ID entered.");
            return;
        }

        BookInfo b = bookMap.get(bid);
        if (b.issuedStatus) {
            System.out.println("Book already issued.");
            return;
        }

        b.issuedStatus = true;
        memberMap.get(mid).takenBooks.add(bid);
        saveRecords();
        System.out.println("Book issued to member.");
    }

    void receiveBook(int mid, int bid) {
        if (!bookMap.containsKey(bid) || !memberMap.containsKey(mid)) {
            System.out.println("Invalid ID entered.");
            return;
        }

        bookMap.get(bid).issuedStatus = false;
        memberMap.get(mid).takenBooks.remove((Integer) bid);
        saveRecords();
        System.out.println("Book returned.");
    }

    void findBook(String keyword) {
        for (BookInfo b : bookMap.values()) {
            if (b.bookTitle.toLowerCase().contains(keyword.toLowerCase()) ||
                b.bookAuthor.toLowerCase().contains(keyword.toLowerCase())) {
                System.out.println(b.bookId + " " + b.bookTitle + " " + b.bookAuthor);
            }
        }
    }

    void loadRecords() {
        try {
            File fb = new File(bookFile);
            if (fb.exists()) {
                BufferedReader br = new BufferedReader(new FileReader(fb));
                String line;
                while ((line = br.readLine()) != null) {
                    String[] p = line.split(",");
                    BookInfo b = new BookInfo(Integer.parseInt(p[0]), p[1], p[2]);
                    b.issuedStatus = Boolean.parseBoolean(p[3]);
                    bookMap.put(b.bookId, b);
                }
                br.close();
            }

            File fm = new File(memFile);
            if (fm.exists()) {
                BufferedReader br = new BufferedReader(new FileReader(fm));
                String line;
                while ((line = br.readLine()) != null) {
                    String[] p = line.split(",");
                    MemberInfo m = new MemberInfo(Integer.parseInt(p[0]), p[1]);
                    memberMap.put(m.memId, m);
                }
                br.close();
            }

        } catch (Exception e) {
            System.out.println("Error loading data.");
        }
    }

    void saveRecords() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(bookFile));
            for (BookInfo b : bookMap.values()) {
                bw.write(b.toData());
                bw.newLine();
            }
            bw.close();

            BufferedWriter bw2 = new BufferedWriter(new FileWriter(memFile));
            for (MemberInfo m : memberMap.values()) {
                bw2.write(m.toData());
                bw2.newLine();
            }
            bw2.close();

        } catch (Exception e) {
            System.out.println("Error saving data.");
        }
    }
}

public class Management_system {
    public static void main(String[] args) {
        LibrarySystem sys = new LibrarySystem();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("1 Add New Book");
            System.out.println("2 Register Member");
            System.out.println("3 Issue a Book");
            System.out.println("4 Return a Book");
            System.out.println("5 Search Book");
            System.out.println("6 Exit");

            int choice = sc.nextInt();

            if (choice == 1) {
                int id = sc.nextInt();
                sc.nextLine();
                String t = sc.nextLine();
                String a = sc.nextLine();
                sys.addNewBook(id, t, a);
            }

            else if (choice == 2) {
                int id = sc.nextInt();
                sc.nextLine();
                String name = sc.nextLine();
                sys.registerMember(id, name);
            }

            else if (choice == 3) {
                int mid = sc.nextInt();
                int bid = sc.nextInt();
                sys.giveBook(mid, bid);
            }

            else if (choice == 4) {
                int mid = sc.nextInt();
                int bid = sc.nextInt();
                sys.receiveBook(mid, bid);
            }

            else if (choice == 5) {
                sc.nextLine();
                String key = sc.nextLine();
                sys.findBook(key);
            }

            else if (choice == 6) {
                sys.saveRecords();
                break;
            }
        }
        sc.close();
    }
}
