public class Product {
    int id;
    String name;

    public Product(int id, String name, String country) {
        this.id = id;
        this.name = name;
    }

    public Product() {
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }


    @Override
    public String toString() {
        return "Producer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
