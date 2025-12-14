package org.brainacad;

import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

public class TestMaven {
    public static void main(String[] args) {
        List<String> fruits = Lists.newArrayList("orange", "banana", "kiwi");
        fruits.forEach(System.out::println);

        System.out.println("---");

        List<String> reverseFruits = Lists.reverse(fruits);
        reverseFruits.forEach(System.out::println);

        System.out.println("---");

        Multimap<String, String> map = ArrayListMultimap.create();
        map.put("key", "firstValue");
        map.put("key", "secondValue");
        System.out.println(map);

        System.out.println("---");

        Properties prop = new Properties();

        try (InputStream is = TestMaven.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (is == null) {
                throw new IllegalStateException("config.properties not found in classpath");
            }
            prop.load(is);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(prop.get("props.local.hello"));
        System.out.println(prop.get("props.mvn.hello"));
    }
}
