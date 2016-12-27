package Organiza;

import javafx.scene.image.ImageView;

public class TableImage
{

    private ImageView image1;
    private ImageView image2;
    private ImageView image3;
    private ImageView image4;
    private ImageView image5;

    public TableImage(ImageView image1, ImageView image2, ImageView image3, ImageView image4, ImageView image5)
    {
        this.image1 = image1;
        this.image2 = image2;
        this.image3 = image3;
        this.image4 = image4;
        this.image5 = image5;
    }

    //Get Set Image 1
    public ImageView getImage1()
    {
        return image1;
    }

    public void setImage1(ImageView image1)
    {
        this.image1 = image1;
    }

    //Get Set Image 2
    public ImageView getImage2()
    {
        return image2;
    }

    public void setImage2(ImageView image2)
    {
        this.image2 = image2;
    }

    //Get Set Image 3
    public ImageView getImage3()
    {
        return image3;
    }

    public void setImage3(ImageView image3)
    {
        this.image3 = image3;
    }

    //Get Set Image 4
    public ImageView getImage4()
    {
        return image4;
    }

    public void setImage4(ImageView image4)
    {
        this.image4 = image4;
    }

    //Get Set Image 5
    public ImageView getImage5()
    {
        return image5;
    }

    public void setImage5(ImageView image5)
    {
        this.image5 = image5;
    }
}
