package hello.MyProject.controller;

import hello.MyProject.entity.Product;
import hello.MyProject.repository.ProductRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ProductController {

    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @PostMapping("/product/save")
    public String saveProduct(
            @RequestParam(required = false) String user_id,  // 로그인안된경우로 임시 구현
            @RequestParam String postName,
            @RequestParam String category,           // HTML select name="category"
            @RequestParam Integer minPrice,
            @RequestParam String deliveryMethod,    // HTML radio name="deliveryMethod"
            @RequestParam(required = false) Integer durationDays,
            @RequestParam(required = false) Integer durationHours,
            @RequestParam(required = false) Integer durationMinutes,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String imageUrl
    ) {
        Product product = new Product();


        // 임시로 게스트 아이디 부여해줌 나중에 수정필요
        if (user_id == null || user_id.isEmpty()) {
            user_id = "guest";
        }
        product.setUser_id(user_id);

        product.setName(postName);
        product.setCategory(category);
        product.setMinPrice(minPrice);
        product.setDeliveryMethod(deliveryMethod);
        product.setDurationDays(durationDays);
        product.setDurationHours(durationHours);
        product.setDurationMinutes(durationMinutes);
        product.setDescription(description);
        product.setImageUrl(imageUrl);

        //System.out.println(">>> saveProduct() 호출됨"); // 확인용 로그
        productRepository.save(product); // DB 저장
        return "redirect:/";
    }

    private String user_id;

    public void setUser_id(String user_id) { this.user_id = user_id; }
    public String getUser_id() { return user_id; }


    @GetMapping("/")
    public String home() {
        return "post"; // templates/post.html
    }
}






