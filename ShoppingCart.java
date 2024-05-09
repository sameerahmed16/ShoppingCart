/*
Pseudocode:
define static filename to store cart data
start infinite loop to display menu and get user input:
display main menu with options: 1. add item to cart 2. remove item from cart 3. save cart to file 4. restore cart from file 5. display items in cart 6. exit program
get user's choice
define function to add item to cart
ask user to select type of item (fruit, vegetable, canned item)
based on selected type, ask for specific details (like name, quantity, weight)
create item and add it to cart
define method in cart class to remove an item
loop through cart items
if an item's name matches provided name, remove that item
define method in cart class to save cart to file
use object serialization to write cart's items to file
define method in cart class to restore cart from file
use object deserialization to read cart's items from file
define method in cart class to display all items
if cart is empty, display "the cart is empty."
otherwise, display each item's name and cost in the cart
 */

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//interface for item information with methods
interface ItemInfo extends Serializable{
    double cost();
    String getName();
}

//class for fruit items, implements ItemInfo and Serializable
class Fruit implements ItemInfo, Serializable {
    private String name;
    private String type;
    private int numOfItems;

    //constructor
    public Fruit(String name, String type, int numOfItems) {
        this.name = name;
        this.type = type;
        this.numOfItems = numOfItems;
    }

    @Override
    public double cost() {
        switch (type.toLowerCase()) {
            //calculating cost
            case "local":
                return numOfItems * 0.5;
            case "tropical":
                return numOfItems * 3.0;
            case "imported":
                return numOfItems * 5.0;
            default:
                return 0;
        }
    }

    @Override
    public String getName() {
        return name;
    }
}

//vegetable class
class Vegetables implements ItemInfo, Serializable {
    private String name;
    private String type;
    private double weight;

    public Vegetables(String name, String type, double weight) {
        this.name = name;
        this.type = type;
        this.weight = weight;
    }

    @Override
    public double cost() {
        //cost calcultion
        switch (type.toLowerCase()) {
            case "leafy green":
                return weight * 0.3;
            case "cruciferous":
                return weight * 0.1;
            case "root":
                return weight * 0.5;
            default:
                return 0;
        }
    }

    @Override
    public String getName() {
        return name;
    }
}

//canned items class
class CannedItem implements ItemInfo, Serializable {
    private String name;
    private int numOfCans;

    public CannedItem(String name, int numOfCans) {
        this.name = name;
        this.numOfCans = numOfCans;
    }

    @Override
    public double cost() {
        return numOfCans * 1.25;
    }

    @Override
    public String getName() {
        return name;
    }
}

//shopping cart class
class Cart implements Serializable {
    private List<ItemInfo> listItems;

    public Cart() {
        listItems = new ArrayList<>();
    }

    //adding an item to the cart
    public void adding(ItemInfo newItem) {
        listItems.add(newItem);
    }
    //remove an item from the cart by name
    public void remove(String itemName) {
        listItems.removeIf(item -> item.getName().equalsIgnoreCase(itemName));
    }

    //save cart using serialization
    public void saveCart(String fileName) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(listItems);
        } catch (IOException e) {
            System.out.println("Failed to save cart: " + e.getMessage());
        }
    }

    //restore the cart to a file using serialization
    public void RestoreFile(String fileName) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            listItems = (List<ItemInfo>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Failed to load cart: " + e.getMessage());
        }
    }

    //displaying
    public void displayItems() {
        if (listItems.isEmpty()) {
            System.out.println("The cart is empty.");
        } else {
            double totalCost = 0;
            for (ItemInfo item : listItems) {
                System.out.println(item.getName() + " - $" + item.cost());
                totalCost += item.cost();
            }
            System.out.println("Total cost: $" + totalCost);
        }
    }
}

//main class
public class ShoppingCart {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        String fileName = "Newcart.ser";
        Cart newCart = new Cart();

        while (true) {
            //menu
            System.out.println("\n1. Add Item to Cart");
            System.out.println("2. Remove Item from Cart");
            System.out.println("3. Save Cart");
            System.out.println("4. Restore Cart");
            System.out.println("5. Display Cart");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");
            int choice = input.nextInt();
            input.nextLine();

            switch (choice) {
                case 1:
                    addItem(input, newCart);
                    break;
                case 2:
                    System.out.print("Enter item name to remove: ");
                    String itemName = input.nextLine();
                    newCart.remove(itemName);
                    break;
                case 3:
                    newCart.saveCart(fileName);
                    break;
                case 4:
                    newCart.RestoreFile(fileName);
                    break;
                case 5:
                    System.out.println("Items in Cart:");
                    newCart.displayItems();
                    break;
                case 6:
                    input.close();
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    //method to add to cart
    private static void addItem(Scanner input, Cart cart) {
        System.out.println("Select type of item to add:");
        System.out.println("1. Fruit");
        System.out.println("2. Vegetable");
        System.out.println("3. Canned Item");
        int type = input.nextInt();
        input.nextLine();

        System.out.print("Enter item name: ");
        String name = input.nextLine();

        switch (type) {
            case 1: //fruit
                System.out.print("Enter type (Local, Tropical, Imported): ");
                String fruitType = input.nextLine();
                System.out.print("Enter number of items: ");
                int num = input.nextInt();
                input.nextLine();
                Fruit fruit = new Fruit(name, fruitType, num);
                cart.adding(fruit);
                break;
            case 2: //vegetable
                System.out.print("Enter type (Leafy Green, Cruciferous, Root): ");
                String vegType = input.nextLine();
                System.out.print("Enter weight: ");
                double weight = input.nextDouble();
                input.nextLine();
                Vegetables vegetable = new Vegetables(name, vegType, weight);
                cart.adding(vegetable);
                break;
            case 3: //canned Item
                System.out.print("Enter number of cans: ");
                int cans = input.nextInt();
                input.nextLine();
                CannedItem cannedItem = new CannedItem(name, cans);
                cart.adding(cannedItem);
                break;
            default:
                System.out.println("Invalid type selected.");
        }
    }
}


