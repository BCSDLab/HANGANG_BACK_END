package in.hangang.enums;

public enum FIleType {

    HANSHOW(0,"show","application/haansoftshow", "https://static.hangang.in/2021/05/13/c361d3cc-38b7-45e1-8d1a-5c81775a8411-1620904836470.png"),
    JPG(1,"jpg","image/jpeg", "https://static.hangang.in/2021/05/13/0e5a316a-b7ed-402d-8050-23de2430e76f-1620904766302.png"),
    HWP(2,"hwp","application/haansofthwp", "https://static.hangang.in/2021/05/13/91e78224-9e48-4394-8146-0bff0e4a27cd-1620904736724.png"),
    PNG(3,"png","image/png" , "https://static.hangang.in/2021/05/13/38a62bc8-0488-4755-9712-32fd3dc50474-1620904799200.png"),
    PDF(4,"pdf","application/pdf", "https://static.hangang.in/2021/05/13/fa15f7f5-2c04-43ef-95ca-9c08e5e8a860-1620904785854.png"),
    TXT(5,"txt","text/plain", "https://static.hangang.in/2021/05/13/1eacceb2-24b7-4442-8f53-d49520e37db4-1620904850309.png"),
    ZIP(6,"zip","application/x-zip-compressed", "https://static.hangang.in/2021/05/13/c73cdf7e-0b40-4053-9634-e41e8f057178-1620904903139.png"),
    EXCEL(7,"xlsx","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "https://static.hangang.in/2021/05/13/2c642bd7-3903-4304-997a-e6c52f9a1d96-1620904887156.png"),
    PPT(8,"ppt","application/vnd.openxmlformats-officedocument.presentationml.presentation", "https://static.hangang.in/2021/05/13/b1d190b8-652e-42e9-bd5e-d634a2d8d075-1620904813458.png"),
    HANCELL(9,"cell","application/haansoftcell", "https://static.hangang.in/2021/05/13/f4c8c711-f9ba-466e-ad2f-621cf824e637-1620904690263.png"),
    WORD(10,"doc","application/vnd.openxmlformats-officedocument.wordprocessingml.document","https://static.hangang.in/2021/05/13/e23117a1-9f09-48d9-9201-dc1e36e6e126-1620904720929.png"),
    DEFAULT(11, "","","https://static.hangang.in/%EA%B0%95%EC%9D%98%EC%9E%90%EB%A3%8C+%EB%94%94%ED%8F%B4%ED%8A%B8+%EC%9D%B4%EB%AF%B8%EC%A7%80.png");



    String ext;
    Integer code;
    String type;
    String url;
    FIleType(Integer code, String ext, String type,String url) {
        this.ext = ext;
        this.code = code;
        this.type = type;
        this.url = url;
    }


    public String getExt(){
        return ext;
    }
    public String getUrL() {
        return url;
    }
    public Integer getCode() {
        return code;
    }
    public String getType() {
        return type;
    }
}
