import java.io.Serializable;

public class MyFile implements Serializable {
    private int id;
    private String name;
    private byte[] data;
    private String fileExtension;
    private String absolutePath;

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public byte[] getData() {
        return data;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public MyFile(int id, String name, byte[] data, String fileExtension, String absolutePath) {
        this.id = id;
        this.name = name;
        //this.data = data;
        this.fileExtension = fileExtension;
        this.absolutePath = absolutePath;
    }
}
