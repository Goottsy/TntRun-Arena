package com.goottsy.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.goottsy.TntRunCore;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

public class CompassGui {
    private List<Player> players;
    private Location destination;
    private List<BossBar> bossbars;
    @Getter
    Entity player;
    private BukkitTask bossBarTask;
    boolean target;


    TntRunCore instance;
    public CompassGui(TntRunCore instance) {
        this.instance = instance;
    }

    public void setPlayer(Entity player){
        this.player=player;
    }
    public void startTravel(List<Player> players, Location destination)
    {
        this.players = players;
        this.destination = destination;

        this.bossbars = new ArrayList<>();

        System.out.println("Destination: " + destination.toString());

        startTravel();
    }

    /*
     * Empieza la fase de ir a la ubicación del inicio del contrato.
     *
     * Durante esta fase, se mostrará en la bossbar la ubicación relativa a donde tienen que ir
     *
     * Una vez alcanzada la ubicación, se lanza la segunda etapa del contrato.
     */
    private void startTravel() {

        for(int i = 0; i < players.size(); i++)
        {
            BossBar bbtmp = Bukkit.createBossBar("", BarColor.WHITE, BarStyle.SOLID);
            bbtmp.addPlayer(players.get(i));

            bossbars.add(bbtmp);
        }

        bossBarTask = Bukkit.getScheduler().runTaskTimer(instance, () -> {
            updateBossBars();
            updateLocation();
        }, 0, 4L);

    }


    /*
     * Proceso en schedule para actualizar las bossbars
     */
    public void updateBossBars()
    {
        for(int i = 0; i < players.size(); i++)
        {
            bossbars.get(i).setTitle(getDirection(players.get(i)) + " " + getDistance(players.get(i))+" "+ instance.getGame().getTextGPS());
        }
    }


    public void updateLocation(){
        if(player!=null && instance.getGame().isTarget()) {
            this.destination = player.getLocation();
        }
    }

    /*
     * Devuelve el unicode relativo a donde tiene que ir el jugador
     */
    public String getDirection(Player player)
    {
        Location playerLocation = player.getLocation();

        // Diferencia en coordenadas
        double dx = destination.getX() - playerLocation.getX();
        double dz = destination.getZ() - playerLocation.getZ();

        // Ángulo hacia el destino
        double angleToDestination = Math.toDegrees(Math.atan2(dz, dx));
        double playerYaw = (playerLocation.getYaw() + 360) % 360; // Normaliza el Yaw
        double relativeAngle = angleToDestination - playerYaw;

        double angle = Math.toDegrees(Math.atan2(dz, dx)) - playerLocation.getYaw();
        angle = normalizeAngle(relativeAngle);

        // Determinar la dirección basada en el ángulo
        if (angle >= -22.5 && angle < 22.5) {
            return "㜁";
        } else if (angle >= 22.5 && angle < 67.5) {
            return "㜂";
        } else if (angle >= 67.5 && angle < 112.5) {
            return "㜃";
        } else if (angle >= 112.5 && angle < 157.5) {
            return "㜄";
        } else if ((angle >= 157.5 && angle <= 180) || (angle >= -180 && angle < -157.5)) {
            return "㜅";
        } else if (angle >= -157.5 && angle < -112.5) {
            return "㜆";
        } else if (angle >= -112.5 && angle < -67.5) {
            return "㜇";
        } else if (angle >= -67.5 && angle < -22.5) {
            return "㜈";
        } else {
            return "㜀";
        }
    }

    /*
     * Devuelve la distancia en bloques del jugador al destino
     */
    public int getDistance(Player player) {
        Location playerLocation = player.getLocation();
        // Asegurarse de que el jugador y el destino estén en el mismo mundo
        if (playerLocation.getWorld().equals(destination.getWorld())) {
            double distance = playerLocation.distance(destination);
            return (int) Math.round(distance); // Redondear a un número entero
        }
        return -1; // Puedes retornar -1 o lanzar una excepción si están en mundos diferentes
    }


    /*
     * HAY QUE ARREGLARLO (tira una excepción pero va)
     */
    public void destroyBossbars()
    {
        if (player != null) {
            if (player instanceof Player) {
                ((Player) player).removePotionEffect(PotionEffectType.GLOWING);
            }
        }

        if(this.bossBarTask!=null){
            bossBarTask.cancel(); // Cancelamos el task y liberamos el scheduler

            for (BossBar bossBar : bossbars) {
                bossBar.removeAll();
                System.out.println("Limpia: " + bossBar.toString());
            }
            System.out.println("Clear bossbars");
            bossbars.clear();
            System.out.println(bossbars.toString());
        }
    }

    private static double normalizeAngle(double angle) {
        while (angle <= -180) angle += 360;
        while (angle > 180) angle -= 360;
        return angle;
    }
}
