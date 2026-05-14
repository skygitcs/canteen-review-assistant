package edu.thu.canteen.data.model;

import java.util.List;

public class Canteen {
    public final long id;
    public final String name;
    public final String coverUrl;
    public final String address;
    public final double avgRating;
    public final double crowdLevel;
    public final List<String> tags;

    public Canteen(long id, String name, String coverUrl, String address,
                   double avgRating, double crowdLevel, List<String> tags) {
        this.id = id;
        this.name = name;
        this.coverUrl = coverUrl;
        this.address = address;
        this.avgRating = avgRating;
        this.crowdLevel = crowdLevel;
        this.tags = tags;
    }
}

