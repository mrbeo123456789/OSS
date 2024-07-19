package com.oss.config;

import com.oss.model.*;
import com.oss.repository.*;
import com.oss.service.CategoryService;
import com.oss.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

@Configuration
public class DataInitializer {

    @Autowired
    ProductRepository productRepository;
    @Autowired
    ProductImageRepository productImageRepository;
    @Autowired
    CategoryService categoryService;


    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, RoleRepository roleRepository, CategoryRepository categoryRepository) {
        return args -> {
            // Initialize roles
            Role adminRole = new Role();
            adminRole.setRoleName("admin");

            Role customerRole = new Role();
            customerRole.setRoleName("customer");

            Role warehouseStaffRole = new Role();
            warehouseStaffRole.setRoleName("warehousestaff");

            Role salesStaffRole = new Role();
            salesStaffRole.setRoleName("salestaff");


            // Save roles to the database
            roleRepository.save(adminRole);
            roleRepository.save(customerRole);
            roleRepository.save(salesStaffRole);
            roleRepository.save(warehouseStaffRole);
            // Initialize categories
            Category shoesCategory = new Category();
            shoesCategory.setCategoryName("Shoes");

            Category racketCategory = new Category();
            racketCategory.setCategoryName("Racket");

            Category clothesCategory = new Category();
            clothesCategory.setCategoryName("Clothes");

            // Save categories to the database
            categoryRepository.save(shoesCategory);
            categoryRepository.save(racketCategory);
            categoryRepository.save(clothesCategory);

            // Initialize and save users with roles
            User adminUser = new User();
            adminUser.setFullName("Admin User");
            adminUser.setUsername("admin");
            adminUser.setEmail("admin@example.com");
            adminUser.setMobile("123456789");
            adminUser.setPassword("$2a$10$Fz7pNp5VYswz4nrVsgsXfOw0J2yXsIW7VxNJ4wC68HFSUTbI6Bfdi");
            adminUser.setRegisteredDate(new Date());
            adminUser.setLastLogin(new Date());
            adminUser.setAvatar("admin-avatar.jpg");
            adminUser.setRole(adminRole);

            userRepository.save(adminUser);

            User customerUser = new User();
            customerUser.setFullName("Customer User");
            customerUser.setUsername("customer");
            customerUser.setEmail("customer@example.com");
            customerUser.setMobile("987654321");
            customerUser.setPassword("$2a$10$Fz7pNp5VYswz4nrVsgsXfOw0J2yXsIW7VxNJ4wC68HFSUTbI6Bfdi");
            customerUser.setRegisteredDate(new Date());
            customerUser.setLastLogin(new Date());
            customerUser.setAvatar("customer-avatar.jpg");
            customerUser.setRole(customerRole);

            userRepository.save(customerUser);

            User warehouseStaffUser = new User();
            warehouseStaffUser.setFullName("Warehouse Staff User");
            warehouseStaffUser.setUsername("warehousestaff");
            warehouseStaffUser.setEmail("warehousestaff@example.com");
            warehouseStaffUser.setMobile("555555555");
            warehouseStaffUser.setPassword("$2a$10$Fz7pNp5VYswz4nrVsgsXfOw0J2yXsIW7VxNJ4wC68HFSUTbI6Bfdi");
            warehouseStaffUser.setRegisteredDate(new Date());
            warehouseStaffUser.setLastLogin(new Date());
            warehouseStaffUser.setAvatar("warehousestaff-avatar.jpg");
            warehouseStaffUser.setRole(warehouseStaffRole);

            userRepository.save(warehouseStaffUser);

            User salesStaffUser = new User();
            salesStaffUser.setFullName("Sales Staff User");
            salesStaffUser.setUsername("salesstaff");
            salesStaffUser.setEmail("salesstaff@example.com");
            salesStaffUser.setMobile("444444444");
            salesStaffUser.setPassword("$2a$10$Fz7pNp5VYswz4nrVsgsXfOw0J2yXsIW7VxNJ4wC68HFSUTbI6Bfdi");
            salesStaffUser.setRegisteredDate(new Date());
            salesStaffUser.setLastLogin(new Date());
            salesStaffUser.setAvatar("salesstaff-avatar.jpg");
            salesStaffUser.setRole(salesStaffRole);

            userRepository.save(salesStaffUser);

            addProducts();


        };
    }

    public void addProducts(String... args) throws Exception {
        // Create products for Shoes
        createProduct("SH001", "Giày Cầu Lông Mizuno Thunder Blade Z - Trắng Đỏ Vàng", "Giày cầu lông Mizuno Thunder Blade Z - Trắng đỏ vàng chính hãng (V1GA237045) là mẫu giày Indoor có trọng lượng siêu nhẹ của Mizuno mong muốn màng đến sự trải nghiệm hoàn hảo nhất cho người chơi. Nhờ lớp đệm cao cấp đã được cải tiền để phù hợp với bàn chân người Châu Á nên mọi chuyển động trên sân sẽ trở nên vô cùng mượt mà và êm ái với giá cả ở mức tầm trung.\n" +
                "\n" +
                "Mẫu giày cổ cao này được thiết kế thêm lớp vòng đệm bên ngoài nhằm cố định phần mắt cá chân, cho phép bạn trải nghiệm sự thoải mái tối đa trong khi chạy. Với Mizuno Thunder Blade Z, bạn có thể hoàn toàn tự tin di chuyển trên khắp mặt sân mà không phải lo ngại các chấn thương.\n" +
                "\n" +
                "Với form giày vừa vặn và trọng lượng được cắt giảm nhẹ nhất, Mizuno Thunder Blade Z giúp tối ưu hóa độ linh hoạt và cảm giác thoải mái khi di chuyển. ", 1790000, 10, 50, categoryService.getCategoryById(Long.valueOf(1)), "https://cdn.shopvnb.com/img/300x300/uploads/gallery/giay-cau-long-mizuno-thunder-blade-z-trang-do-vang-chinh-hang-v1ga237045_1692993090.webp");
        createProduct("SH002", "Giày Cầu Lông Mizuno Gate Sky Plus 3 - Đen Hồng Chính Hãng", "Giày cầu lông Mizuno Gate Sky Plus 3 - Đen hồng chính hãng là mẫu giày tầm trung thuộc dòng GATE SKY PLUS, đây là dòng giày cầu lông ở phân khúc tầm trung và hướng tới những người chơi mong muốn một đôi giày ổn định, êm ái, đa năng và có mức giá hợp lý.\n" +
                "\n" +
                "Với phương châm không bao giờ dừng lại, Mizuno đã dày công nghiên cứu và mang tới mẫu giày GATE SKY PLUS 3 với những thay đổi mang tính đột phá, nâng cấp toàn diện hơn so với các phiên bản trước.", 1800000, 20, 40, categoryService.getCategoryById(Long.valueOf(1)), "https://cdn.shopvnb.com/uploads/gallery/giay-cau-long-mizuno-gate-sky-plus-3-den-hong-chinh-hang-71ga234025_1692992825.webp");
        createProduct("SH003", "Giày Cầu Lông Mizuno Wave Claw 2 - Vàng Chanh Hồng Chính Hãng", "Giày cầu lông Mizuno Wave Claw 2 - Vàng chanh hồng chính hãng nằm trong series nhẹ nhất trong dòng WAVE CLAW đã được cải tiến để trở nên nhẹ hơn các phiên bản trước. Ngoài ra, giờ đây nó còn có MIZUNO ENERZY để mang lại khả năng đệm và hoàn trả năng lượng tuyệt vời.\n" +
                "\n" +
                "Kết cấu giày được thay đổi ở phần gót chân tạo cảm giác dễ chịu, thoải mái trong quá trình di chuyển và đặc biệt là công nghệ mới Mizuno Enerzy lần đầu tiên được áp dụng mang lại những trải nghiệm ấn tượng cho các VĐV.", 2900000, 20, 30, categoryService.getCategoryById(Long.valueOf(1)), "https://cdn.shopvnb.com/uploads/gallery/giay-cau-long-mizuno-wave-claw-2-vang-chanh-hong-chinh-hang-71ga211023_1692913820.webp");

        // Create products for Racket
        createProduct("RA001", "Vợt Cầu Lông Victor Auraspeed HS Plus - Xách Tay", "Vợt Cầu Lông Victor Auraspeed HS Plus - Xách Tay là một trong những cây vợt cầu lông mang cho mình thiết kế nổi bật, tích hợp nhiều công nghệ tiên tiến. Nếu các lông thủ muốn 1 cây vợt hứa hẹn đem đến những trải nghiệm bức phá trên sân cầu thì Victor Auraspeed HS Plus là một sự lựa chọn đáng cân nhắc\n" +
                "\n" +
                "Victor Auraspeed HS Plus mang cả thân vợt và khung vợt được làm từ High Resilience Modulus Graphite là chất liệu có độ bền cao về khả năng chịu lực, chịu va đập. Kết hợp cùng với vật liệu Pyrofil, cấu trúc carbon đa lớp Hard Core Technology cho khả năng hấp thụ sốc cao, giảm rung chấn cho các pha cầu ổn định và độ chính xác cao hơn.\n" +
                "\n" +
                "Với  các công nghệ ưu việt, hàng đầu sẽ được tích hợp trên cây vợt đảm bảo sẽ mang đến những trải nghiệm hoàn hảo nhất dành cho người chơi. Trong đó, nổi trội nhất phải kể đến sự tích hợp của WES 3.0 nâng cấp và trục có khả năng chống xoắn cao giúp tạo ra các đòn tấn công với các cú đập cắm và cầu đi nhanh nằm trong sự kiểm soát của người chơi.\n" +
                "\n" +
                "Với thiết kế phối màu đen chủ đạo mạnh mẽ cùng với họa tiết đỏ, xám, xanh dương tạo nên trên nền nhám tạo nên một tổng thể bắt mắt không kém phần quyền lực, chắc chắn sẽ làm hài lòng cho các lông thủ.\n" +
                "\n" +
                "Và nếu bạn là một người yêu thích lối đánh thiên công với những pha tấn công bất ngờ thì đảm bảo Victor Auraspeed HS Plus chính là sự lựa chọn đáng cân nhắc. Đây là dòng vợt hơi nặng đầu, cùng thân vợt cứng sẽ cho những pha đập cầu mạnh mẽ. ", 4450000, 20, 20, categoryService.getCategoryById(Long.valueOf(2)), "https://cdn.shopvnb.com/uploads/gallery/vot-cau-long-victor-auraspeed-hs-plus-xach-tay_1713233077.webp");
        createProduct("RA002", "BVợt Cầu Lông Kumpoo Power Control 520BS Chính Hãng", "Vợt cầu lông Kumpoo Power Control 520BS chính hãng nổi bật với thông số 4U, điểm cân bằng 295mm, thân vợt có độ cứng ở mức trung bình hướng đến đối tượng người chơi mới, đang tập làm quen với các động tác cơ bản đang tìm kiếm cho mình một cây vợt cầu lông giá rẻ, chất lượng.\n" +
                "\n" +
                "Siêu phẩm Vợt cầu lông Kumpoo Power Control 520BS chính hãng chắc chắn sẽ làm các lông thủ cực ưng ý ngay từ cái nhìn đầu tiên với thiết kế vô cùng sắc sảo cùng phối màu cực bắt mắt. Với tông màu Tím- Xanh phối với các chi tiết xanh lá vô cùng tỉ mỉ giúp vợt toát lên vẻ huyền bí nhưng không kém phần hiện đại, thu hút mọi lông thủ từ cái nhìn đầu tiên.\n" +
                "\n" +
                "Vợt có chiều dài đạt chuẩn 675mm như các siêu phẩm trên thị trường hiện nay cộng với sức căng tối đa 24LBS (10,8kg) khá hoàn hảo dành cho các người chơi mới tập làm quen với bộ môn này.", 69.99, 45, 25, categoryService.getCategoryById(Long.valueOf(2)), "https://cdn.shopvnb.com/img/300x300/uploads/gallery/vot-cau-long-kumpoo-power-control-520bs-chinh-hang-1_1713213817.webp");
        createProduct("RA003", "Vợt Cầu Lông Yonex Astrox 02 Feel Chính Hãng", "Vợt cầu lông Yonex Astrox 02 Feel chính hãng được thiết kế mang lại sức mạnh và sự cân bằng vượt trội nhờ công nghệ tiên tiến. Công nghệ phân bổ trọng lượng của vợt một cách hiệu quả, cung cấp cho người chơi khả năng giữ thăng bằng tốt hơn để thực hiện các cú đánh chính xác như bắt lười, bỏ nhỏ. Vợt được thiết kế đặc biệt để đáp ứng nhu cầu của những người chơi với những pha nhồi cầu liên tục. Hệ thống RGS phân chia trọng lượng của vợt giữa tay cầm và khung trên, tăng cường tốc độ và khả năng kiểm soát.\n" +
                "\n" +
                "Với lớp sơn được phối giữa xanh lá và hồng tạo nên một ngoại hình bắt mắt, độc lạ nhưng cũng không kém phần quyến rũ.", 1239000, 15, 15, categoryService.getCategoryById(Long.valueOf(2)), "https://cdn.shopvnb.com/uploads/gallery/vot-cau-long-yonex-astrox-02-feel-1_1712887566.webp");

        }

    private void createProduct(String code, String name, String description, double price, int sales, int quantity, Category category, String imageUrl) {
        Product product = new Product();
        product.setProductCode(code);
        product.setProductName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setSales(sales);
        product.setAddedDate(new Date());
        product.setQuantity(quantity);
        product.setCategory(category);

        ProductImage image = new ProductImage();
        image.setImageUrl(imageUrl);
        image.setProduct(product);

        productRepository.save(product);
        productImageRepository.save(image);
    }
}
