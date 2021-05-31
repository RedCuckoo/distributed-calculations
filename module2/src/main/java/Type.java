import java.util.Date;

public class Type {
    int id;
    String name;
    int productId;

    public Type(int id, String name, int productId) {
        this.id = id;
        this.name = name;
        this.productId = productId;
    }

    @Override
    public String toString() {
        return "Souvenir{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", productId=" + productId +
                '}';
    }

    public Type() {
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getProductId() {
        return productId;
    }
}
