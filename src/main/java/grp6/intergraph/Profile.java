package grp6.intergraph;

public class Profile {
    private String name;
    private int id;

    public Profile(String name, int id){
        this.name=name;
        this.id=id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name=name;
    }
}
