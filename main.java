import java.util.*;

class Product {
    private String name;
    private double price;
    private int quantity;

    public Product(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTotal() {
        return price * quantity;
    }
}

abstract class Discount {
    abstract double apply(double total);
}

class FestiveDiscount extends Discount {
    @Override
    double apply(double total) {
        return total * 0.9;
    }
}

class BulkDiscount extends Discount {
    private int totalQuantity;

    public BulkDiscount(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    @Override
    double apply(double total) {
        if (totalQuantity > 5) {
            return total * 0.8;
        } else {
            return total;
        }
    }
}

interface Payment {
    void pay(double amount, Scanner scanner);
}

class UPIPayment implements Payment {
    @Override
    public void pay(double amount, Scanner scanner) {
        System.out.println("Total Amount Payable via UPI: " + amount);
    }
}

class CreditCardPayment implements Payment {
    @Override
    public void pay(double amount, Scanner scanner) {
        System.out.println("Total Amount Payable via Credit Card: " + amount);
    }
}

class CashPayment implements Payment {
    @Override
    public void pay(double amount, Scanner scanner) {
        System.out.println("Total Amount Payable via Cash: " + amount);
    }
}

class PayLaterPayment implements Payment {
    @Override
    public void pay(double amount, Scanner scanner) {
        System.out.println("Amount to Pay Later: " + amount);
    }
}

class CombinePayment implements Payment {
    @Override
    public void pay(double amount, Scanner scanner) {
        int friends = scanner.nextInt();
        double share = amount / (friends + 1);
        System.out.println("Total Amount: " + amount);
        System.out.println("Number of friends: " + friends);
        System.out.println("Each person pays: " + share);
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int N = scanner.nextInt();
        List<Product> cart = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            String name = scanner.next();
            double price = scanner.nextDouble();
            int quantity = scanner.nextInt();
            cart.add(new Product(name, price, quantity));
        }
        String discountType = scanner.next();
        String paymentType = scanner.next();
        String coupon = scanner.next();
        int referrals = scanner.nextInt();

        double total = 0;
        int totalQuantity = 0;
        for (Product p : cart) {
            total += p.getTotal();
            totalQuantity += p.getQuantity();
        }

        Discount discount;
        if (discountType.equals("festive")) {
            discount = new FestiveDiscount();
        } else if (discountType.equals("bulk")) {
            discount = new BulkDiscount(totalQuantity);
        } else {
            discount = new Discount() {
                @Override
                double apply(double total) {
                    return total;
                }
            };
        }

        double finalAmount = discount.apply(total);

        if (coupon.equals("SAVE10")) {
            finalAmount *= 0.9;
        }

        for (Product p : cart) {
            System.out.println("Product: " + p.getName() + ", Price: " + p.getPrice() + ", Quantity: " + p.getQuantity());
        }

        Payment payment;
        if (paymentType.equals("upi")) {
            payment = new UPIPayment();
        } else if (paymentType.equals("credit")) {
            payment = new CreditCardPayment();
        } else if (paymentType.equals("cash")) {
            payment = new CashPayment();
        } else if (paymentType.equals("later")) {
            payment = new PayLaterPayment();
        } else if (paymentType.equals("combine")) {
            payment = new CombinePayment();
        } else {
            payment = new CashPayment(); // default
        }
        payment.pay(finalAmount, scanner);

        int credits = 0;
        credits += referrals * 1;
        if (paymentType.equals("upi")) {
            credits += 10;
        }
        System.out.println("Total Credits Earned: " + credits);
    }
}