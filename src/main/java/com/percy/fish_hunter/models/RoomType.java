package com.percy.fish_hunter.models;

public enum RoomType {

    TWO {
        @Override
        public int getNumberOfPlayerByType() {
            return 2;
        }
    }, QUAD {
        @Override
        public int getNumberOfPlayerByType() {
            return 4;
        }
    };

    public abstract int getNumberOfPlayerByType();
}
