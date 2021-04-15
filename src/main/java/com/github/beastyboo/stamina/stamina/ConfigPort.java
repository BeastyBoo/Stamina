package com.github.beastyboo.stamina.stamina;

import space.arim.dazzleconf.annote.ConfComments;
import space.arim.dazzleconf.annote.ConfDefault;
import space.arim.dazzleconf.annote.ConfHeader;
import space.arim.dazzleconf.annote.ConfKey;
import space.arim.dazzleconf.sorter.AnnotationBasedSorter;

import java.util.Map;

@ConfHeader({"This plugin is created a managed by BeastCraft3/BeastyBoo\n"})
public interface ConfigPort {

    @AnnotationBasedSorter.Order(20)
    @ConfDefault.DefaultDouble(20.0)
    @ConfKey("max-stamina-default")
    @ConfComments({"Represents the max stamina a player can have"})
    double maxStaminaDefault();

    @AnnotationBasedSorter.Order(30)
    @ConfDefault.DefaultMap({"stamina.rank.vip", "25.0"})
    @ConfKey("max-stamina-rank-values")
    @ConfComments({"Different values can be used for different ranks", "Give ranks the permission 'stamina.rank.KEY' for different stamina values"})
    Map<String, Double> rankMap();

    @AnnotationBasedSorter.Order(40)
    @ConfDefault.DefaultDouble(0.1)
    @ConfKey("recovery-rate")
    @ConfComments({"The amount of stamina the player regains per second"})
    double recoveryRate();

    @AnnotationBasedSorter.Order(50)
    @ConfDefault.DefaultDouble(0.2)
    @ConfKey("tire-rate")
    @ConfComments({"Tire rate (the amount of stamina lost per 1 tick = 50ms)"})
    double tireRate();

    @AnnotationBasedSorter.Order(60)
    @ConfDefault.DefaultDouble(3.0)
    @ConfKey("slowness-threshold")
    @ConfComments({"If stamina is less than this value, player will be slowed"})
    double slownessThreshold();

    @AnnotationBasedSorter.Order(80)
    @ConfDefault.DefaultDouble(1.0)
    @ConfKey("jump-depletion-value")
    @ConfComments({"Deplete stamina on player jump value"})
    double jumpDepletionValue();

}
