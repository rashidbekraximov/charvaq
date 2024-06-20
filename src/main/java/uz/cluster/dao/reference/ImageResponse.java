package uz.cluster.dao.reference;

public class ImageResponse {
    private String imageName;
    private String imageData;

    // Constructors, getters, and setters

    public ImageResponse() {}

    public ImageResponse(String imageName, String imageData) {
        this.imageName = imageName;
        this.imageData = imageData;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageData() {
        return imageData;
    }

    public void setImageData(String imageData) {
        this.imageData = imageData;
    }
}
