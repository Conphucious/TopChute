package io.github.conphucious.topchute.model.map;


import java.io.File;

public record BoardTile(String id, String name, String description, BoardTileAction boardTileAction, File imgFile) {
}
