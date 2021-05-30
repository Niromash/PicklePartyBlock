package fr.niromash.partyblock.utils;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

public class ListUtils {

    public static <T> Predicate<T> distinctBy(Function<? super T, ?> f) {
        Set<Object> objects = new HashSet<>();
        return t -> objects.add(f.apply(t));
    }
}
