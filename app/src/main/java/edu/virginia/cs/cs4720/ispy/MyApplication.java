package edu.virginia.cs.cs4720.ispy;

import com.parse.Parse;

/**
 * Created by Cole on 11/23/2015.
 */
public class MyApplication extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(this, "LODmoenJcpDk44AAQzVVXym6wASNhosUAqq5V5JO", "MPq7ajGm8JnFfRJK127nYYTNEjf99RBZ8hKLE8ED");
    }
}
