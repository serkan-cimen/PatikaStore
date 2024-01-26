package view;

import controller.BrandController;
import controller.CategoryContorller;
import controller.ProductController;
import model.Brand;
import model.Category;
import model.Product;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class PatikaStore {

    private static final Scanner scan = new Scanner(System.in);
    private CategoryContorller categoryContorller;
    private BrandController brandController;
    private ProductController productController;

    public PatikaStore(CategoryContorller categoryContorller, BrandController brandController, ProductController productController) {
        this.categoryContorller = categoryContorller;
        this.brandController = brandController;
        this.productController = productController;
    }

    public void storeMenu() {
        String str =
                "--------------------------------------\n" +
                        "PatikaStore Ürün Yönetim Paneli!\n" +
                        "1- Kategori Lİstele,\n" +
                        "2- Marka Lİstele,\n" +
                        "3- Ürün Lİstele,\n" +
                        "0- Çıkış Yap!\n" +
                        "--------------------------------------";
        System.out.println(str);

        int prefence = getIntegerFromMinToMaxFromUser(0, 3, "Seçiminiz: ");

        switch (prefence) {
            case 1 -> categoryOperationsMenu();
            case 2 -> brandOperationsMenu();
            case 3 -> productOperationsMenu();
            case 0 -> exitTheProgram();
        }
    }

    private void categoryOperationsMenu() {
        String str =
                "---------------------------------\n" +
                        "PatikaStore Kategori Yönetim Paneli!\n" +
                        "1- Tüm Kategorlileri Listele,\n" +
                        "2- Ürünleri Kategoriye Göre Listele,\n" +
                        "3- Yeni Kategori Ekle,\n" +
                        "4- Kategori Sil,\n" +
                        "0- Ana Menüye Dön!\n" +
                        "---------------------------------";
        System.out.println(str);

        int preference = getIntegerFromMinToMaxFromUser(0, 4, "Seçiminiz: ");

        switch (preference) {
            case 1 -> listAllCategories();
            case 2 -> listProductByCategory();
            case 3 -> addNewCategory();
            case 4 -> deleteCategory();
            case 0 -> storeMenu();
        }
    }

    public void listAllCategories() {
        List<Category> categories = categoryContorller.getCategories();

        if (categories.size() == 0) {
            System.out.println("Kategori yok!");
            categoryOperationsMenu();
            return;
        }

        System.out.println("----------------------------\nKategoriler...\n----------------------------");

        System.out.printf("| %1$-4s| %2$-20s|%n","ID","Kategori Adı");

        for (Category c : categories) {
            System.out.printf("| %1$-4d| %2$-20s|%n",c.getCategoryId(),c.getCategoryName());
        }

        System.out.println("----------------------------");

        categoryOperationsMenu();
    }

    public void listProductByCategory() {
        List<Category> categories = categoryContorller.getCategories();

        if (categories.size() == 0) {
            System.out.println("Kategori yok!");
            categoryOperationsMenu();
            return;
        }

        System.out.println("----------------------------\nKategorliler...\n----------------------------");

        System.out.printf("| %1$-4s| %2$-20s|%n","ID","Kategori Adı");

        for (Category c : categories) {
            System.out.printf("| %1$-4d| %2$-20s|%n",c.getCategoryId(),c.getCategoryName());
        }

        System.out.println("----------------------------");

        Category category = null;
        while (category == null) {
            int preference = getIntegerFromMinToMaxFromUser(0, categoryContorller.getLastCategoryId(), "Ürünlerini listelemek istediğiniz kategori numarasını giriniz: ");

            for (Category c : categories) {
                if (c.getCategoryId() == preference) {
                    category = c;
                    break;
                }
            }
        }

        List<Product> products = productController.getProducts();

        System.out.println("----------------------------------------------\nÜrünler...\n----------------------------------------------");

        System.out.printf("| %1$-4s| %2$-20s| %3$-15s|%n","ID","Ürün  Adı", "Kategori");

        for (Product p : products) {
            if (p.getProductCategory().equals(category))
                System.out.printf("| %1$-4d| %2$-20s| %3$-15s| %n",p.getProductId(),p.getProductName(), p.getProductCategory().getCategoryName());
        }

        System.out.println("----------------------------------------------");

        categoryOperationsMenu();
    }

    public void addNewCategory() {
        System.out.print("Lütfen yeni kategori adı ekleyiniz : ");
        String name = scan.nextLine();

        if (categoryContorller.getCategoryByName(name) != null) {
            System.out.println("Kategori adı (" + name + ") zaten var.");
        } else {
            categoryContorller.addNewCategory(name);

            System.out.println("Yeni kategori \"" + name + "\" eklendi.");
        }

        categoryOperationsMenu();
    }

    public void deleteCategory() {
        List<Category> categories = categoryContorller.getCategories();

        if (categories.size() == 0) {
            System.out.println("Kategori yok!");
            categoryOperationsMenu();
            return;
        }

        System.out.println("----------------------------\nKategoriler...\n----------------------------");

        System.out.printf("| %1$-4s| %2$-20s|%n", "ID", "Kategori adı");

        for (Category c : categories) {
            System.out.printf("| %1$-4s| %2$-20s|%n",c.getCategoryId(),c.getCategoryName());
        }

        System.out.println("----------------------------");

        Category category = null;
        while (category == null) {
            int preference = getIntegerFromMinToMaxFromUser(0, categoryContorller.getLastCategoryId(), "Silmek istediğiniz kategorinin numarasını giriniz: ");

            for (Category c : categories) {
                if (c.getCategoryId() == preference) {
                    category = c;
                    break;
                }
            }
        }

        String repeatingMessage = "Kategori \"" + category.getCategoryName() + "\" ve \"İlgili Tüm Ürürnler\" silinecek. Emin misiniz? (1-Evet, 2-Hayır) : ";
        int selected = getIntegerFromMinToMaxFromUser(1, 2, repeatingMessage);

        if (selected == 1) {
            for (Product p : productController.getProducts()) {
                if (p.getProductCategory().equals(category)) {
                    p.setProductCategory(null);
                }
            }
            categoryContorller.deleteCategory(category);

            System.out.println("Kategori silindi.");

            productController.deleteUnbrandedProducts();
        } else {
            System.out.println("Silme işlemi iptal edildi...");
        }

        categoryOperationsMenu();
    }


    private void brandOperationsMenu() {
        String str=
                "------------------------------\n" +
                        "PatikaStore Marka Yönetim Paneli!\n" +
                        "1- Tüm Markaları Listele,\n" +
                        "2- Ürünleri Markaya Göre Listele,\n" +
                        "3- Yeni Marka Ekle,\n" +
                        "4- Marka Sil,\n" +
                        "0- Ana Menüye Dön!\n" +
                        "------------------------------";
        System.out.println(str);

        int preference = getIntegerFromMinToMaxFromUser(0,4,"Seçiminiz: ");

        switch (preference) {
            case 1 -> listAllBrands();
            case 2 -> listAllProductsByBrand();
            case 3 -> addNewBrand();
            case 4 -> deleteBrand();
            case 0 -> storeMenu();
        }
    }

    private void listAllBrands() {
        List<Brand> brands = brandController.getBrands();

        if (brands.size() == 0) {
            System.out.println("Marka yok!");
            brandOperationsMenu();
            return;
        }

        System.out.println("----------------------------\nMarkalar...\n----------------------------");

        System.out.printf("| %1$-4s| %2$-20s|%n","ID","Marka Adı");

        for (Brand b : brands) {
            System.out.printf("| %1$-4d| %2$-20s|%n",b.getBrandId(),b.getBrandName());
        }

        System.out.println("----------------------------");

        brandOperationsMenu();
    }

    private void listAllProductsByBrand() {
        List<Brand> brands = brandController.getBrands();

        if (brands.size() == 0) {
            System.out.println("Marka yok!");
            brandOperationsMenu();
            return;
        }

        System.out.println("----------------------------\nMarkalar...\n----------------------------");

        System.out.printf("| %1$-4s| %2$-20s|%n","ID","Marka Adı");

        for (Brand b : brands) {
            System.out.printf("| %1$-4d| %2$-20s|%n",b.getBrandId(),b.getBrandName());
        }

        System.out.println("----------------------------");

        Brand brand = null;
        while (brand == null) {
            int preference = getIntegerFromMinToMaxFromUser(0, brandController.getLastBrandId(), "Ürünlerini listelemek istediğiniz marka numarasını giriniz: ");

            for (Brand b : brands) {
                if (b.getBrandId() == preference) {
                    brand = b;
                    break;
                }
            }
        }

        List<Product> products = productController.getProducts();

        System.out.println("----------------------------------------------\nÜrünler...\n----------------------------------------------");

        System.out.printf("| %1$-4s| %2$-20s| %3$-15s|%n","ID","Ürün Adı", "Kategori");

        for (Product p : products) {
            if (p.getProductBrand().equals(brand))
                System.out.printf("| %1$-4d| %2$-20s| %3$-15s| %n",p.getProductId(),p.getProductName(), p.getProductCategory().getCategoryName());
        }

        System.out.println("----------------------------------------------");

        brandOperationsMenu();
    }

    private void addNewBrand() {
        System.out.print("Lütfen yeni marka adını giriniz : ");
        String name = scan.nextLine();

        if (brandController.getBrandByName(name) != null) {
            System.out.println("Marka adı (" + name + ") zaten var.");
        } else {
            brandController.getBrandByName(name);

            System.out.println("Yeni marka \"" + name + "\" eklendi.");
        }

        brandOperationsMenu();
    }

    private void deleteBrand() {
        List<Brand> brands = brandController.getBrands();

        if (brands.size() == 0) {
            System.out.println("Marka yok!");
            brandOperationsMenu();
            return;
        }

        System.out.println("----------------------------\nMarkalar...\n----------------------------");

        System.out.printf("| %1$-4s| %2$-20s|%n","ID","Marka adı");

        for (Brand b : brands) {
            System.out.printf("| %1$-4d| %2$-20s|%n",b.getBrandId(), b.getBrandName());
        }

        System.out.println("----------------------------");

        Brand brand = null;
        while (brand == null) {
            int preference = getIntegerFromMinToMaxFromUser(0, brandController.getLastBrandId(),"Silmek istediğiniz markanın kodunu yazınız: " );

            for (Brand b : brands) {
                if (b.getBrandId() == preference) {
                    brand = b;
                    break;
                }
            }
        }

        String repeaitingMessage = "Brand \"" + brand.getBrandId() + "\" and \"All Related Product\" will be deleted. Are you sure? (1-Yes, 2-No) : ";
        int selected = getIntegerFromMinToMaxFromUser(1,2, repeaitingMessage);

        if (selected == 1) {
            for (Product p : productController.getProducts()) {
                if (p.getProductBrand().equals(brand)) {
                    p.setProductBrand(null);
                }
            }
            brandController.deleteBrand(brand);

            System.out.println("Brand deleted.");

            productController.deleteUnbrandedProducts();
        } else {
            System.out.println("Deletion canceled...");
        }

        brandOperationsMenu();
    }

    private void productOperationsMenu() {
        String str =
                "--------------------------------\n" +
                        "Patika Ürün Yönetim Paneli!\n" +
                        "1- List All Products,\n" +
                        "2- Add New Product,\n" +
                        "3- Delete Product,\n" +
                        "0- Back To Main Menu!\n" +
                        "--------------------------------";
        System.out.println(str);

        int preference = getIntegerFromMinToMaxFromUser(0,3,"Your Preference: ");

        switch (preference)
        {
            case 1 -> listAllProducts();
            case 2 -> addNewProduct();
            case 3 -> deleteProduct();
            case 0 -> storeMenu();
        }
    }

    private void listAllProducts() {
        List<Product> products = productController.getProducts();

        System.out.println("----------------------------\nPRODUCTS...\n----------------------------");

        System.out.printf("| %1$-4s| %2$-20s|%n","ID","PRODUCT NAME");

        for (Product p : products) {
            System.out.printf("| %1$-4d| %2$-20s|%n",p.getProductId(),p.getProductName());
        }

        System.out.println("----------------------------");

        productOperationsMenu();
    }

    private void addNewProduct() {

        double price, discount;
        int stock;
        System.out.println("Enter the properties of the product you want to add.");

        System.out.println("Ürün adını giriniz: ");
        String name = scan.nextLine().trim();

        System.out.print("Ürün markasını giriniz: ");
        String brandName = scan.nextLine().trim();

        System.out.print("Ürün kategorisini giriniz: ");
        String categoryName = scan.nextLine().trim();

        System.out.print("Ekran boyutunu giriniz: ");
        String screenSize = scan.nextLine().trim();

        System.out.print("RAM kapasitesini giriniz (GB) : ");
        String ramCapacity = scan.nextLine().trim();

        System.out.print("Dahili bellek kapasitesini giriniz (GB) : ");
        String internalStorage = scan.nextLine().trim();

        System.out.print("Renk giriniz : ");
        String color = scan.nextLine().trim();

        System.out.print("Batarya kapasitesini giriniz : ");
        String batteryCapacity = scan.nextLine().trim();

        price = getDoubleFromMinToMaxFromUser(0,Double.MAX_VALUE,"Enter the price (only numeric value) : ");

        discount = getDoubleFromMinToMaxFromUser(0, 100, "Enter the discount (only numeric value between 0-100) : ");

        stock = getIntegerFromMinToMaxFromUser(0, Integer.MAX_VALUE, "Enter the stock quantity (Integer 0 and above only) : ");

        Brand brand = brandController.getBrandByName(brandName);
        if (brand == null) {
            brand = brandController.addAndGetBrand(brandName);
        }

        Category category = categoryContorller.getCategoryByName(categoryName);
        if (category == null) {
            category = categoryContorller.addAndGetCategory(categoryName);
        }

        productController.addNewProduct(name, category, price, discount, stock, brand, internalStorage, screenSize, ramCapacity, color, batteryCapacity);

        productOperationsMenu();
    }

    private void deleteProduct() {
        List<Product> products = productController.getProducts();

        System.out.println("----------------------------\nPRODUCTS...\n----------------------------");

        System.out.printf("| %1$-4s| %2$-20s|%n","ID","PRODUCT NAME");

        for (Product p : products) {
            System.out.printf("| %1$-4d| %2$-20s|%n",p.getProductId(),p.getProductName());
        }

        System.out.println("----------------------------");

        Product product = null;
        while (product == null) {
            int preference = getIntegerFromMinToMaxFromUser(0, productController.getLastProductId(),"Enter the product id you want to delete: " );

            for (Product p : products) {
                if (p.getProductId() == preference) {
                    product = p;
                    break;
                }
            }
        }

        String repeatingMessage = "Product \"" + product.getProductName() + "\" will be deleted. Are you sure? (1-Yes, 2-No) : ";
        int selected = getIntegerFromMinToMaxFromUser(1,2, repeatingMessage);

        if (selected == 1) {
            productController.deleteProduct(product);

            System.out.println("Product deleted.");
        } else  {
            System.out.println("Product canceled...");
        }

        productOperationsMenu();
    }

    private void exitTheProgram() {
        System.out.println("Exiting the program...");
    }

    private int getIntegerFromMinToMaxFromUser(int min, int max, String repeatingMessage) {
        int selection;
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print(repeatingMessage);
            try {
                selection = scanner.nextInt();
                if (selection >= min && selection <= max)
                    break;
                else System.out.println("Invalid entry!");
            } catch (InputMismatchException e) {
                System.out.println("Invalid entry!");
                scanner.next();
            }
        }
        return selection;
    }

    private double getDoubleFromMinToMaxFromUser(double min, double max, String repeatingMessage) {
        double selection;
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print(repeatingMessage);
            try {
                selection = scan.nextDouble();
                if (selection >= min && selection <= max)
                    break;
                else System.out.println("Invalid entry!");
            } catch (InputMismatchException e) {
                System.out.println("Invalid entry!");
                scanner.next();
            }
        }
        return selection;
    }
}
