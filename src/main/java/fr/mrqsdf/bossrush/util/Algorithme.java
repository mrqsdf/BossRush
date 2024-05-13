package fr.mrqsdf.bossrush.util;

import fr.mrqsdf.bossrush.component.MobsComponent;
import fr.mrqsdf.bossrush.component.PlayerComponent;
import fr.mrqsdf.bossrush.res.MobAction;
import fr.mrqsdf.engine2d.jade.GameObject;

import java.util.Random;

public class Algorithme {

    public static MobAction mobAlgorithm(GameObject mob, GameObject player){
        MobsComponent mobComponent = mob.getComponent(MobsComponent.class);
        PlayerComponent playerComponent = player.getComponent(PlayerComponent.class);
        int mobDamage = mobComponent.getDamage();
        int mobHeal = mobComponent.getHeal();
        int mobMaxHeal = mobComponent.getMaxHeal();
        int playerHeal = playerComponent.getHeal();
        int playerMaxHeal = playerComponent.getMaxHeal();
        int playerDamage = playerComponent.getDamage();

        // Constantes
        final double CRITICAL_CHANCE = mobComponent.criticalChance;

        int healthDifference = playerHeal - mobDamage;
        int enemyHealthDifference = mobHeal - playerDamage;

        // Probabilités de chaque action basées sur les différences de points de vie
        double attackProbability = calculateProbability(healthDifference);
        double defendProbability = calculateDefendProbability(enemyHealthDifference, mobComponent.isDefending);
        double healProbability = calculateHealProbability(healthDifference, enemyHealthDifference);

        // Utilisation de Math.random()
        double rd = Math.random();

        if (rd < attackProbability){
            if (Math.random() < CRITICAL_CHANCE){
                return MobAction.CRITICAL;
            }
            return MobAction.ATTACK;
        } else if (rd < attackProbability + defendProbability){
            return MobAction.DEFEND;
        } else if (rd < attackProbability + defendProbability + healProbability){
            return MobAction.HEAL;
        }
        return MobAction.ATTACK;
    }

    // Fonction pour calculer la probabilité d'attaque en fonction de la différence de points de vie
    private static double calculateProbability(int healthDifference){
        // Plus la différence est grande, plus la probabilité d'attaque est élevée
        // Vous pouvez ajuster cette fonction selon votre logique spécifique
        return Math.max(0.1, 1.0 - (healthDifference * 0.1));
    }

    // Fonction pour calculer la probabilité de soin en fonction des différences de points de vie
    private static double calculateHealProbability(int healthDifference, int enemyHealthDifference){
        // Plus la différence entre la santé du joueur et celle de l'ennemi est grande, plus la probabilité de soin est élevée
        // Vous pouvez ajuster cette fonction selon votre logique spécifique
        double maxDifference = Math.max(healthDifference, enemyHealthDifference);
        return Math.max(0.1, 1.0 - (maxDifference * 0.1));
    }

    private static double calculateDefendProbability(int enemyHealthDifference, boolean isDefending){
        // Si l'ennemi est déjà en état de défense, réduire la probabilité de défense
        double defendProbability = 0.2; // Probabilité de base de défense
        if (isDefending) {
            defendProbability *= 0.5; // Réduction de la probabilité de moitié si déjà en défense
        }
        // Plus la différence entre la santé du joueur et celle de l'ennemi est grande, plus la probabilité de défense est élevée
        // Vous pouvez ajuster cette fonction selon votre logique spécifique
        double maxDifference = Math.max(0, enemyHealthDifference);
        return defendProbability * Math.max(0.1, 1.0 - (maxDifference * 0.1));
    }

}
