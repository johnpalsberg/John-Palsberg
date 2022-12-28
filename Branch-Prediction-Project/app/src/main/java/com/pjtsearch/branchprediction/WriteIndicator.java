package com.pjtsearch.branchprediction;

public final class WriteIndicator {
    public static sealed interface SumType permits WriteMemory, WriteRegister, WritePC {

    }

    public static final record WriteMemory(int address, int data) implements SumType {}
    public static final record WriteRegister(int address, int data) implements SumType {}
    public static final record WritePC(int pcVal) implements SumType {}


}
