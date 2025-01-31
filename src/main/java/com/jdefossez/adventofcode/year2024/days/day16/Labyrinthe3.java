package com.jdefossez.adventofcode.year2024.days.day16;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Labyrinthe3 {

    // Directions possibles : Droite, Bas, Gauche, Haut
    private static final int[] DX = {1, 0, -1, 0};
    private static final int[] DY = {0, 1, 0, -1};

    // Classe représentant un état (position + direction précédente)
    static class Etat {
        Coord coord;
        int casesParcourues, bifurcations, directionPrecedente;

        Etat(Coord coord, int casesParcourues, int bifurcations, int directionPrecedente) {
            this.coord = coord;
            this.casesParcourues = casesParcourues;
            this.bifurcations = bifurcations;
            this.directionPrecedente = directionPrecedente;
        }

        public int getCost() {
            return casesParcourues + 1000 * bifurcations;
        }

        @Override
        public String toString() {
            return String.format("(%d,%d) %d", coord.getX(), coord.getY(), getCost());
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            Etat etat = (Etat) o;
            return casesParcourues == etat.casesParcourues && bifurcations == etat.bifurcations && directionPrecedente == etat.directionPrecedente && Objects.equals(coord, etat.coord);
        }

        @Override
        public int hashCode() {
            return Objects.hash(coord, casesParcourues, bifurcations, directionPrecedente);
        }
    }

    // Classe représentant un chemin
    static class Chemin {
        List<Etat> etats;

        Chemin(List<Etat> etats) {
            this.etats = etats;
        }

        public int getCost() {
            return etats.getLast().getCost();
        }

        @Override
        public String toString() {
            return etats.stream().map(Etat::toString).collect(Collectors.joining(",", "", " => " + String.valueOf(getCost())));
        }
    }

    // Méthode pour résoudre le problème du chemin le plus court avec coûts combinés
    public static int[] dijkstraAvecBifurcations(List<List<String>> labyrinthe, int startX, int startY, int endX, int endY) {
        int n = labyrinthe.size();
        int m = labyrinthe.getFirst().size();

        // pour chaque case, tous les chemins qui y mènent
        Map<Coord, List<Chemin>> chemins = new HashMap<>();
        Coord start = new Coord(startX, startY);
        Etat initialEtat = new Etat(start, 0, 0, 0);
        Chemin initialChemin = new Chemin(List.of(initialEtat));
        chemins.put(start, List.of((initialChemin)));

        int[][][] visited = new int[n][m][4]; // [cases parcourues, bifurcations]
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                visited[i][j][0] = Integer.MAX_VALUE;
                visited[i][j][1] = Integer.MAX_VALUE;
                visited[i][j][2] = Integer.MAX_VALUE;
                visited[i][j][3] = Integer.MAX_VALUE;
            }
        }

        PriorityQueue<Etat> pq = new PriorityQueue<>(Comparator.comparingInt(Etat::getCost));

        // Initialisation du point de départ
        pq.add(new Etat(start, 0, 0, 0));

        // Dijkstra
        while (!pq.isEmpty()) {
            Etat curr = pq.poll();

            if (curr.coord.getX() == endX && curr.coord.getY() == endY) {
                continue;
            }


            // Explorer les voisins
            for (int dir = 0; dir < 4; dir++) {
                if (dir == (curr.directionPrecedente + 2) % 4) continue;

                int nx = curr.coord.getX() + DX[dir];
                int ny = curr.coord.getY() + DY[dir];

                if (nx >= 0 && nx < n && ny >= 0 && ny < m
                        && (labyrinthe.get(ny).get(nx).equals(".") || labyrinthe.get(ny).get(nx).equals("E"))) {
                    // Calcul du nouveau coût
                    int newCasesParcourues = curr.casesParcourues + 1;
                    int newBifurcations = curr.bifurcations + (dir != curr.directionPrecedente ? 1 : 0);
                    int newCout = newCasesParcourues + 1000 * newBifurcations;

                    // si pas déjà parcourue avec cette direction
                    if (newCout <= visited[nx][ny][dir] && newCout < visited[nx][ny][(dir + 2) % 4]) {
                        visited[nx][ny][dir] = newCout;


                        Coord key = new Coord(nx, ny);

                        Etat newEtat = new Etat(key, newCasesParcourues, newBifurcations, dir);
                        if (!pq.contains(newEtat)) {
                            pq.add(newEtat);
                        }

                        chemins.putIfAbsent(key, new ArrayList<>());
                        chemins.get(key).addAll(
                                chemins.get(curr.coord)
                                       .stream()
                                       .map(chemin -> chemin.etats)
                                       .map(etats -> {
                                           List<Etat> updatedEtats = new ArrayList<>(etats);
                                           updatedEtats.add(newEtat);
                                           return new Chemin(updatedEtats);
                                       })
                                       .toList()
                        );
                    }
                }
            }
        }

        if (chemins.containsKey(new Coord(endX, endY))) {
            System.out.println("Found it");
            System.out.println(chemins.get(new Coord(endX, endY)));
        } else {
            System.out.println("Not found it");
        }
        int minCost = chemins.get(new Coord(endX, endY)).stream()
                             .mapToInt(Chemin::getCost)
                             .min()
                             .getAsInt();

        var toto = chemins.get(new Coord(endX, endY))
                          .stream()
                          .filter(chemin -> chemin.getCost() == minCost)
                          .flatMap(chemin -> chemin.etats.stream().map(etat -> etat.coord))
                          .collect(Collectors.toSet());

        System.out.println(toto.size());

        // Aucun chemin trouvé
        return new int[]{-1, -1};
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        Path path = Paths.get(Objects.requireNonNull(Labyrinthe3.class.getClassLoader().getResource("input/2024/day_16.txt")).toURI());
        List<String> lines = Files.lines(path).toList();

        List<List<String>> labyrinthe = initGrid(lines);

        // Définir les points de départ et de destination
        Coord[] startAndEnd = initStartAndEnd(labyrinthe);
        Coord start = startAndEnd[0];
        Coord end = startAndEnd[1];

        // Calculer tous les chemins les plus courts
        int[] result = dijkstraAvecBifurcations(labyrinthe, start.getX(), start.getY(), end.getX(), end.getY());

        int minCost = result[0] + 1000 * result[1];
        System.out.println("Cout : " + minCost);
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

        return new Coord[]{start, end};
    }
}
