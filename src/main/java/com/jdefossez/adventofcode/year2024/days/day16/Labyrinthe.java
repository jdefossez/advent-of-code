package com.jdefossez.adventofcode.year2024.days.day16;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Labyrinthe {

    // Directions possibles : Droite, Bas, Gauche, Haut
    private static final int[] DX = {1, 0, -1, 0};
    private static final int[] DY = {0, 1, 0, -1};
//    private static final String[] DIRECTION_NAMES = {"Droite", "Bas", "Gauche", "Haut"};

    // Classe représentant un état (position + direction précédente)
    static class Etat {
        int x, y, casesParcourues, bifurcations, directionPrecedente;

        Etat(int x, int y, int casesParcourues, int bifurcations, int directionPrecedente) {
            this.x = x;
            this.y = y;
            this.casesParcourues = casesParcourues;
            this.bifurcations = bifurcations;
            this.directionPrecedente = directionPrecedente;
        }
    }

    static class Chemin {
        List<int[]> cellules;
        int casesParcourues;
        int bifurcations;

        Chemin(List<int[]> cellules, int casesParcourues, int bifurcations) {
            this.cellules = cellules;
            this.casesParcourues = casesParcourues;
            this.bifurcations = bifurcations;
        }
    }

    // Méthode pour résoudre le problème du chemin le plus court avec coûts combinés
    public static int[] dijkstraAvecBifurcations(List<List<String>> labyrinthe, int startX, int startY, int endX, int endY) {
        int n = labyrinthe.size();
        int m = labyrinthe.getFirst().size();

        // Tableau pour stocker le coût minimum (cases parcourues, bifurcations) pour chaque cellule
        int[][][] dist = new int[n][m][2]; // [cases parcourues, bifurcations]
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                dist[i][j][0] = Integer.MAX_VALUE;
                dist[i][j][1] = Integer.MAX_VALUE;
            }
        }

        // File de priorité pour Dijkstra : (casesParcourues, bifurcations, x, y, direction)
        PriorityQueue<Etat> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a.casesParcourues + 1000 * a.bifurcations));

        Map<Coord, List<Coord>> predecessors = new HashMap<>();

        // Initialisation du point de départ
        pq.add(new Etat(startX, startY, 0, 0, 0));

        // Dijkstra
        while (!pq.isEmpty()) {
            Etat curr = pq.poll();

            // Si on atteint la destination, retourner le coût
            if (curr.x == endX && curr.y == endY) {
                for (int i = 0; i < dist.length; i++) {
                    for (int i1 = 0; i1 < dist[i].length; i1++) {
                        if (dist[i][i1][0] != Integer.MAX_VALUE) {
                            // System.out.printf("(%d, %d)[%d, %d] %s\n", i, i1, dist[i][i1][0], dist[i][i1][1], dist[i][i1][0] + 1000 * dist[i][i1][1]);
                            System.out.print(dist[i][i1][0] + 1000 * dist[i][i1][1]);
                            System.out.print(",");
                        } else {
                            System.out.print(",");
                        }
                    }
                    System.out.println();
                }
                predecessors.forEach((coord, coords) -> System.out.println(coord + ": " + coords));
                return new int[]{curr.casesParcourues, curr.bifurcations};
            }

            // Explorer les voisins
            for (int i = 0; i < 4; i++) {
                int nx = curr.x + DX[i];
                int ny = curr.y + DY[i];

                if (nx >= 0 && nx < n && ny >= 0 && ny < m && (labyrinthe.get(ny).get(nx).equals(".") || labyrinthe.get(ny).get(nx).equals("E"))) {
                    // Calcul du nouveau coût
                    int newCasesParcourues = curr.casesParcourues + 1;
                    int newBifurcations = curr.bifurcations + (i != curr.directionPrecedente ? 1 : 0);
                    int newCout = newCasesParcourues + 1000 * newBifurcations;

                    int oldCout = dist[nx][ny][0] + 1000 * dist[nx][ny][1];
                    // Si le nouveau coût est meilleur, mettre à jour et ajouter à la file de priorité
//                    if (newCasesParcourues < dist[nx][ny][0] ||
//                            (newCasesParcourues == dist[nx][ny][0] && newBifurcations < dist[nx][ny][1])) {
                    if (newCout < oldCout) {
                        Coord key = new Coord(nx, ny);
                        predecessors.putIfAbsent(key, new ArrayList<>());
                        predecessors.get(key).add(new Coord(curr.x, curr.y));
                        dist[nx][ny][0] = newCasesParcourues;
                        dist[nx][ny][1] = newBifurcations;
                        pq.add(new Etat(nx, ny, newCasesParcourues, newBifurcations, i));
                    }
                }
            }
        }

        // Aucun chemin trouvé
        return new int[]{-1, -1};
    }

    public static void main(String[] args) throws IOException, URISyntaxException {

        Path path = Paths.get(Objects.requireNonNull(Labyrinthe.class.getClassLoader().getResource("input/2024/day_16_test3.txt")).toURI());
        List<String> lines = Files.lines(path).toList();

        List<List<String>> labyrinthe = initGrid(lines);

        // Définir les points de départ et de destination
        Coord[] startAndEnd = initStartAndEnd(labyrinthe);
        Coord start = startAndEnd[0];
        Coord end = startAndEnd[1];

        // System.out.printf("Start: %s, End: %s", start, end);

        // Calculer le chemin le plus court en tenant compte des bifurcations
        int[] result = dijkstraAvecBifurcations(labyrinthe, start.getX(), start.getY(), end.getX(), end.getY());
        if (result[0] == -1) {
            System.out.println("Aucun chemin trouvé.");
        } else {
            System.out.println("Coût total : " + result[0] + " cases, " + result[1] + " bifurcations = " + (result[0] + 1000 * result[1]));
        }
    }

    private static List<List<String>> initGrid(List<String> lines) {
        return lines.stream()
                    .map(line -> Arrays.stream(line.split("")).collect(Collectors.toList()))
                    .toList();
    }

    private static Coord[] initStartAndEnd(List<List<String>> grid) {
        Coord start = null;
        Coord end = null;

        for (int i = 0; i < grid.size(); i++) {
            for (int j = 0; j < grid.get(i).size(); j++) {
                if (grid.get(i).get(j).equals("S")) {
                    start = new Coord(j, i);
                } else if (grid.get(i).get(j).equals("E")) {
                    end = new Coord(j, i);
                }
            }
        }

        return List.of(start, end).toArray(new Coord[0]);
    }


}
