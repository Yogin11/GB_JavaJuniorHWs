package ru.geekbrains.lesson1.task2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

/**
 * Корзина
 *
 * @param <T> Еда
 */
public class Cart<T extends Food> {

    /**
     * Товары в магазине
     */
    private final ArrayList<T> foodstuffs;
    private final UMarket market;
    private final Class<T> clazz;
    private HashMap<AtomicBoolean, Predicate<? super Food>> mapIngredients;

    public Cart(Class<T> clazz, UMarket market) {
        this.clazz = clazz;
        this.market = market;
        foodstuffs = new ArrayList<>();
        mapIngredients = new HashMap<>();
    }

    public Collection<T> getFoodstuffs() {
        return foodstuffs;
    }

    /**
     * Распечатать список продуктов в корзине
     */
    public void printFoodstuffs() {
        AtomicInteger index = new AtomicInteger(1);
        foodstuffs.forEach(food -> {
            System.out.printf("[%d] %s (Белки: %s Жиры: %s Углеводы: %s)\n",
                    index.getAndIncrement(), food.getName(),
                    food.getProteins() ? "Да" : "Нет",
                    food.getFats() ? "Да" : "Нет",
                    food.getCarbohydrates() ? "Да" : "Нет");
        });
    }

    /**
     * Балансировка корзины
     */
    public void cardBalancing() {
        AtomicBoolean proteins = new AtomicBoolean(false);
        AtomicBoolean fats = new AtomicBoolean(false);
        AtomicBoolean carbohydrates = new AtomicBoolean(false);
        mapIngredients.put(proteins, Food::getProteins);
        mapIngredients.put(fats, Food::getFats);
        mapIngredients.put(carbohydrates, Food::getCarbohydrates);

        int[] counter = {1};
        for (AtomicBoolean isNutrientIncluded : mapIngredients.keySet()) {
            //Проверяем наличие ингредиента в продуктах корзины
            checkIngredients(isNutrientIncluded);
            //Если ингредиента нет:
            if (!isNutrientIncluded.get()) {
                market.getThings(Food.class).stream()
                        .filter(mapIngredients.get(isNutrientIncluded))
                        .findAny()
                        .ifPresentOrElse(item -> {
                            System.out.println("Для балансировки добавляем новый продукт " +
                                               item.getName());
                            foodstuffs.add((T) item);
                            isNutrientIncluded.set(true);
                        }, () -> System.out.println("Нет продуктов с необходимым ингредиентом - " +
                                                    "невозможно сбалансировать корзину по БЖУ."));
            } else {
                counter[0]++;
            }
        }
        if (counter[0] == 3) {
            System.out.println("Корзина уже сбалансирована по БЖУ.");
            return;
        }

        if (proteins.get() && fats.get() && carbohydrates.get())
            System.out.println("Корзина сбалансирована по БЖУ.");
        else
            System.out.println("Невозможно сбалансировать корзину по БЖУ.");
    }

    /**
     * Метод проверки и установки наличия ингредиента в продуктах корзины
     */
    public void checkIngredients(AtomicBoolean isNutrientIncluded) {
        foodstuffs.stream()
                .filter(mapIngredients.get(isNutrientIncluded))
                .findAny()
                .ifPresent(x -> {
                    isNutrientIncluded.set(true);
                });
    }
}
