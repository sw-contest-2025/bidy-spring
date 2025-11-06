package hello.MyProject.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer productId;

    @Column(nullable = false)
    private String user_id = "guest"; // 일단 임시로 아이디 설정함

    @Column(nullable = false)
    private String postName;

    @Column(nullable = false)
    private String category;         // Enum 대신 String

    @Column(nullable = false)
    private Integer minPrice;

    @Column(name = "deliveryMethod")
    private String deliveryMethod;   // Enum 대신 String

    @Column(name = "durationDays")
    private Integer durationDays;
    @Column(name = "durationHours")
    private Integer durationHours;
    @Column(name = "durationMinutes")
    private Integer durationMinutes;

    @Column(name = "description")
    private String description;

    @Column(name = "imageUrl")
    private String imageUrl;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private java.sql.Timestamp createdAt;

    // 기본 생성자
    public Product() {}

    public void setUser_id(String user_id) { this.user_id = user_id; }
    public void setName(String postName) { this.postName = postName; }
    public void setCategory(String category) { this.category = category; }
    public void setMinPrice(Integer minPrice) { this.minPrice = minPrice; }
    public void setDeliveryMethod(String deliveryMethod) { this.deliveryMethod = deliveryMethod; }
    public void setDurationDays(Integer durationDays) { this.durationDays = durationDays; }
    public void setDurationHours(Integer durationHours) { this.durationHours = durationHours; }
    public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }
    public void setDescription(String description) { this.description = description; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }


}







