package com.airwallex.codechallenge;

import com.airwallex.codechallenge.alerter.SpotAlerter;
import com.airwallex.codechallenge.input.CurrencyConversionRate;
import com.airwallex.codechallenge.input.Reader;
import com.airwallex.codechallenge.output.Writer;
import org.reflections.Reflections;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class App {
    private static final Reflections reflections = new Reflections("com.airwallex.codechallenge.alerter");

    public static void main(String[] args) {
        final List<SpotAlerter> alerters = reflections.getSubTypesOf(SpotAlerter.class)
                .stream()
                .map(App::createAlerter)
                .collect(toList());
        Writer writer = new Writer();
        Reader reader = new Reader();
        reader
                .read(args[0])
                .filter(rate -> rate !=  CurrencyConversionRate.Factory.getINVALID())
                .flatMap(rate -> alerters.stream().flatMap(alerter ->
                        alerter.process(new CurrencyConversionRate[]{rate}).stream()))
                .forEach(alert -> System.out.println(writer.write(alert)));
    }

    private static SpotAlerter createAlerter(Class<? extends SpotAlerter> cls) {
        try {
            return cls.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(String.format("Failed to create alerter of type %s", cls), e);
        }
    }
}
