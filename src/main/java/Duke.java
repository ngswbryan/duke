import java.util.Scanner;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/** 
 * Main class of the programme
 *  */ 

public class Duke {
    
    private Storage storage;
    private TaskList tasks;
    private Ui ui;

    public Duke() throws FileNotFoundException {
        ui = new Ui();
        this.storage = new Storage("../duke.txt");
        this.tasks = new TaskList(storage.load());
    }

    public void run() throws DukeException, FileNotFoundException{
        Scanner sc = new Scanner(System.in);
        ArrayList<Task> store = this.tasks.list;

        System.out.println("Hello! I'm Duke\n" + "What can I do for you?");
        try {
            while(true) {
                String next = sc.next();
                if (next.equals("list")) {
                   ui.list(store);
                } else if (next.equals("done")) {
                    int taskNo = sc.nextInt();
                    if (taskNo > store.size()) {
                        throw new DukeException("☹ OOPS!!! No such item in the list!");
                    }
                    store.get(taskNo-1).markAsDone();
                    this.storage.write(this.tasks);
                    System.out.println("Nice! I've marked this task as done: ");
                    System.out.println("  " + store.get(taskNo-1).toString());
                } else if (next.equals("bye")) {
                    ui.bye();
                    break;
                } else if (next.equals("todo") || next.equals("deadline") || next.equals("event")){
                    Task newTask;
                    String remainder = sc.nextLine();
                    if (next.equals("todo")) {
                        if (remainder.trim().isEmpty()) {
                            throw new DukeException("☹ OOPS!!! The description of a todo cannot be empty.");
                        }
                        newTask = new Todo(remainder.trim());
                    } else if (next.equals("deadline")) {
                        int position = remainder.indexOf("/");
                        if (position == -1) {
                            throw new DukeException("☹ OOPS!!! Not a valid deadline command");
                        }
                        String formattedDate = Parser.getFormattedDate(remainder.substring(position+3));
                        newTask = new Deadline(remainder.substring(0,position).trim(), formattedDate);
                    } else {
                        int position = remainder.indexOf("/");
                        if (position == -1) {
                            throw new DukeException("☹ OOPS!!! Not a valid event command");
                        }
                        String formattedDate = Parser.getFormattedDate(remainder.substring(position+3));
                        newTask = new Event(remainder.substring(0,position).trim(), formattedDate);
                    }
                    store.add(newTask);
                    this.storage.write(this.tasks);
                    System.out.println("Got it. I've added this task: ");
                    System.out.println("  " + newTask.toString());
                    String size = Integer.toString(store.size());
                    System.out.println("Now you have " + size + " tasks in the list.");
                } else if (next.equals("delete")){
                    int taskNo = sc.nextInt();
                    if (taskNo > store.size()) {
                        throw new DukeException("☹ OOPS!!! No such item in the list!");
                    }
                    System.out.println("Noted. I've removed this task: ");
                    System.out.println("  " + store.get(taskNo-1).toString());
                    store.remove(taskNo-1);
                    this.storage.write(this.tasks);
                    String size = Integer.toString(store.size());
                    System.out.println("Now you have " + size + " tasks in the list.");
                } else if (next.equals("find")) {
                    String search = sc.nextLine().trim();
                    ArrayList<Task> found = this.tasks.find(search);
                    int n = 1;
                    if(found.isEmpty()){
                            System.out.println("No matching items found");
                    } else {
                        System.out.println("Here are the matching tasks in your list:");
                        for (Task item : found) {
                            System.out.println(n + "." + item);
                            n++;
                        }
                    }
                } else {
                    throw new DukeException("☹ OOPS!!! I'm sorry, but I don't know what that means :-(");
                }
            }
        } catch(DukeException err) {
            System.out.println(err.getMessage());
        } finally {
            sc.close();
        }
    }

    public static void main(String[] args) throws DukeException, FileNotFoundException {
        Duke dukeObject = new Duke();
        dukeObject.run(); 
    }
}
