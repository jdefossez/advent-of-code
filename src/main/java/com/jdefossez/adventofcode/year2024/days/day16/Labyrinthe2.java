package com.jdefossez.adventofcode.year2024.days.day16;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Labyrinthe2 {

    // Directions possibles : Droite, Bas, Gauche, Haut
    private static final int[] DX = {1, 0, -1, 0};
    private static final int[] DY = {0, 1, 0, -1};

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

    // Classe représentant un chemin
    static class Chemin {
        List<int[]> cellules;
        int casesParcourues;
        int bifurcations;

        Chemin(List<int[]> cellules, int casesParcourues, int bifurcations) {
            this.cellules = cellules;
            this.casesParcourues = casesParcourues;
            this.bifurcations = bifurcations;
        }

        public int getCost() {
            return casesParcourues + 1000 * bifurcations;
        }

        @Override
        public String toString() {
            return cellules.stream().map(t -> String.format("(%s,%s) %s", t[0], t[1], getCost())).collect(Collectors.joining());
        }
    }

    // Méthode pour résoudre le problème du chemin le plus court avec coûts combinés
    public static Map<String, List<Chemin>> dijkstraAvecBifurcations(List<List<String>> labyrinthe, int startX, int startY, int endX, int endY) {
        int n = labyrinthe.size();
        int m = labyrinthe.get(0).size();

        // Tableau pour stocker les coûts minimums (cases parcourues, bifurcations) pour chaque cellule
        int[][][] dist = new int[n][m][2]; // [cases parcourues, bifurcations]
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                dist[i][j][0] = Integer.MAX_VALUE;
                dist[i][j][1] = Integer.MAX_VALUE;
            }
        }

        // Liste de chemins pour chaque cellule (coordonnées) atteinte avec coût minimal
        Map<String, List<Chemin>> chemins = new HashMap<>();

        // File de priorité pour Dijkstra : (casesParcourues, bifurcations, x, y, direction)
        PriorityQueue<Etat> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a.casesParcourues + 1000 * a.bifurcations));

        // Initialisation du point de départ
        pq.add(new Etat(startX, startY, 0, 0, 0));
        dist[startY][startX][0] = 0; // cases parcourues
        dist[startY][startX][1] = 0; // bifurcations

        // Ajouter le point de départ à la liste des chemins
        List<int[]> startPath = new ArrayList<>();
        startPath.add(new int[]{startX, startY});
        chemins.put(startX + "," + startY, new ArrayList<>(List.of(new Chemin(startPath, 0, 0))));

        // Dijkstra
        while (!pq.isEmpty()) {
            Etat curr = pq.poll();

            // Si on atteint la destination, continuer pour explorer d'autres chemins
            if (curr.x == endX && curr.y == endY) continue;

            // Explorer les voisins
            for (int i = 0; i < 4; i++) {
                int nx = curr.x + DX[i];
                int ny = curr.y + DY[i];

                if (nx >= 0 && nx < n && ny >= 0 && ny < m && (labyrinthe.get(ny).get(nx).equals(".") || labyrinthe.get(ny).get(nx).equals("E"))) {
                    // Calcul du nouveau coût
                    int newCasesParcourues = curr.casesParcourues + 1;
                    int newBifurcations = curr.bifurcations + (i != curr.directionPrecedente ? 1 : 0);
                    int newCout = newCasesParcourues + 1000 * newBifurcations;

                    // Si le nouveau coût est inférieur, mettre à jour et ajouter à la file de priorité
                    if (newCout < dist[ny][nx][0] + 1000 * dist[ny][nx][1]) {
                        dist[ny][nx][0] = newCasesParcourues;
                        dist[ny][nx][1] = newBifurcations;
                        pq.add(new Etat(nx, ny, newCasesParcourues, newBifurcations, i));

                        // Mettre à jour les chemins
                        List<int[]> newPath = new ArrayList<>();
                        newPath.add(new int[]{nx, ny});
                        List<Chemin> newChemins = new ArrayList<>();
                        newChemins.add(new Chemin(newPath, newCasesParcourues, newBifurcations));
                        chemins.put(nx + "," + ny, newChemins);
                    }
                    // Si le coût est égal, ajouter ce chemin aussi
                    else if (newCout == dist[ny][nx][0] + 1000 * dist[ny][nx][1]) {
                        // Ajouter les chemins existants avec ce coût
                        List<Chemin> cheminsExistants = chemins.get(nx + "," + ny);
                        List<Chemin> cheminsTmp = new ArrayList<>();
                        for (Chemin chemin : cheminsExistants) {
                            List<int[]> newPath = new ArrayList<>(chemin.cellules);
                            newPath.add(new int[]{nx, ny});
//                            cheminsTmp.get(nx + "," + ny).add(new Chemin(newPath, newCasesParcourues, newBifurcations));
                            cheminsTmp.add(new Chemin(newPath, newCasesParcourues, newBifurcations));
                        }
                        chemins.get(nx + "," + ny).addAll(cheminsTmp);
                    }
                }
            }
        }

        // Récupérer tous les chemins menant à la destination
        System.out.println(chemins);
//        return chemins.getOrDefault(endX + "," + endY, new ArrayList<>());
        return chemins;
    }


    // parcourir le labyrinthe en partant de la fin et en prenant uniquement des chemins avec un poids décroissant par rapport au noeud courant ou à son précédent 
    public static List<Chemin> dijkstraAvecBifurcationsReverse(Map<String, List<Chemin>> chemins, int startX, int startY, int endX, int endY) {

        Chemin start = chemins.get(startX + "," + startY).getFirst();

        // File de priorité pour Dijkstra : (casesParcourues, bifurcations, x, y, direction)
        List<Chemin> pq = new ArrayList<>();

        int count = 0;
        // Initialisation du point de départ
//        pq.add(start);
        count++;

        for (int i = 0; i < 4; i++) {
            int nx = startX + DX[i];
            int ny = startY + DY[i];

            if (chemins.containsKey(nx + "," + ny) && chemins.get(nx + "," + ny).getFirst().getCost() < start.getCost()) {
                pq.add(chemins.get(nx + "," + ny).getFirst());
                count++;
            }
        }

        // Dijkstra
        while (!pq.isEmpty()) {
            Chemin curr = pq.removeFirst();

            // Si on atteint la destination, continuer pour explorer d'autres chemins
            if (curr.cellules.getFirst()[0] == endX && curr.cellules.getFirst()[1] == endY) continue;

            // Explorer les voisins
            System.out.printf("inspect (%d,%d): ", curr.cellules.getFirst()[0], curr.cellules.getFirst()[1]);
            for (int i = 0; i < 4; i++) {
                int nx = curr.cellules.getFirst()[0] + DX[i];
                int ny = curr.cellules.getFirst()[1] + DY[i];

                if (chemins.containsKey(nx + "," + ny) && chemins.get(nx + "," + ny).getFirst().getCost() < (curr.getCost())) {
                    pq.add(chemins.get(nx + "," + ny).getFirst());
                    System.out.printf("(%d,%d)", nx, ny);
                    count++;
                }
            }
            System.out.println();
        }

        // Récupérer tous les chemins menant à la destination
//        System.out.println(chemins);
        System.out.println("Count : " + count);
        return chemins.getOrDefault(endX + "," + endY, new ArrayList<>());
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        Path path = Paths.get(Objects.requireNonNull(Labyrinthe2.class.getClassLoader().getResource("input/2024/day_16_test.txt")).toURI());
        List<String> lines = Files.lines(path).toList();

        List<List<String>> labyrinthe = initGrid(lines);

        // Définir les points de départ et de destination
        Coord[] startAndEnd = initStartAndEnd(labyrinthe);
        Coord start = startAndEnd[0];
        Coord end = startAndEnd[1];

        // Calculer tous les chemins les plus courts
        Map<String, List<Chemin>> result = dijkstraAvecBifurcations(labyrinthe, start.getX(), start.getY(), end.getX(), end.getY());

        dijkstraAvecBifurcationsReverse(result, end.getX(), end.getY(), start.getX(), start.getY());
        System.out.println();
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