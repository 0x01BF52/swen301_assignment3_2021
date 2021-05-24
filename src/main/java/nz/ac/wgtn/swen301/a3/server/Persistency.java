package nz.ac.wgtn.swen301.a3.server;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Persistency {
    private List<String> list;

    public Persistency() {
        this.list = new CopyOnWriteArrayList<>();
    }

    public Boolean add(String s) {
        return this.list.add(s);
    }

}
