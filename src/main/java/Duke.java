import java.util.Scanner;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Duke {

    private Storage storage;
    private TaskList tasks;

    public Duke() throws FileNotFoundException {
        this.storage = new Storage("../duke.txt");
        this.tasks = new TaskList(storage.load());
    }

    public static void main(String[] args) throws DukeException, FileNotFoundException{
        Scanner sc = new Scanner(System.in);
        Duke test = new Duke();
        ArrayList<Task> store = test.tasks.list;

        System.out.println("Hello! I'm Duke\n" + "What can I do for you?");
        try {
            while(true) {
                String next = sc.next();
                if (next.equals("list")) {
                    if (store.size() == 0) {
                        throw new DukeException("☹ OOPS!!! Your list is still empty!");
                    }
                    System.out.println("Here are the tasks in your list:");
                    for (int i = 0; i < store.size(); i++) {
                        int taskNumber = i + 1;
                        String taskString = Integer.toString(taskNumber);
                        System.out.println(taskString + ".[" + store.get(i).getStatusIcon() + "] " + store.get(i).toString());
                    }
                } else if (next.equals("done")) {
                    int taskNo = sc.nextInt();
                    if (taskNo > store.size()) {
                        throw new DukeException("☹ OOPS!!! No such item in the list!");
                    }
                    store.get(taskNo-1).markAsDone();
                    test.storage.write(test.tasks);
                    System.out.println("Nice! I've marked this task as done: ");
                    System.out.println("  " + store.get(taskNo-1).toString());
                } else if (next.equals("bye")) {
                    System.out.println("Bye. Hope to see you again soon!");
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
                        newTask = new Deadline(remainder.substring(0,position).trim(), remainder.substring(position+3).trim());
                    } else {
                        int position = remainder.indexOf("/");
                        if (position == -1) {
                            throw new DukeException("☹ OOPS!!! Not a valid event command");
                        }
                        newTask = new Event(remainder.substring(0,position).trim(), remainder.substring(position+3).trim());
                    }
                    store.add(newTask);
                    test.storage.write(test.tasks);
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
                    test.storage.write(test.tasks);
                    String size = Integer.toString(store.size());
                    System.out.println("Now you have " + size + " tasks in the list.");
                } else {
                    throw new DukeException("☹ OOPS!!! I'm sorry, but I don't know what that means :-(");
                }
            }
        } catch(DukeException err) {
            System.out.println(err.getMessage());
        }

    }
}
