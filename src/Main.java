import controller.BrandController;
import controller.CategoryContorller;
import controller.ProductController;
import view.PatikaStore;

public class Main {
    public static void main(String[] args) {
        CategoryContorller categoryContorller = new CategoryContorller();
        BrandController brandController = new BrandController();
        ProductController productController = new ProductController(categoryContorller, brandController);

        PatikaStore patikaStore = new PatikaStore(categoryContorller, brandController, productController);

        patikaStore.storeMenu();
    }
}