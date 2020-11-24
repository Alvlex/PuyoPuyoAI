package appTest;

import java.util.Collection;
import java.util.HashMap;

public class Templates {

    private HashMap<String, Template> templates;

    public Templates(){
        templates = new HashMap<>();
        templates.put("chain1", new Template("chain1.csv"));
        templates.put("empty", new Template("empty.csv"));
        templates.put("full", new Template("full.csv"));
        templates.put("chain4", new Template("chain4.csv"));
    }

    public Template get(String name){
        return templates.get(name);
    }

    public Collection<Template> getAll(){
        return templates.values();
    }

}
