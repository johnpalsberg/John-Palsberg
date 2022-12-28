package com.pjtsearch.branchprediction;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class DecoderTest {
    @Test
    void shouldThrowOnEmpty() {
        assertThrows(AssertionError.class, () -> Decoder.decode(""));
        assertThrows(IllegalArgumentException.class, () -> Decoder.decode("ADD"));
    }

    @Test
    void shouldDecodeAdd() throws IOException {
        assertEquals(new Instruction.Add(18, 8, 9), Decoder.decode("ADD,s2,s0,s1"));
        assertThrows(IllegalArgumentException.class, () -> Decoder.decode("ADD,s2,s0"));
        assertThrows(IllegalArgumentException.class, () -> Decoder.decode("ADD,s2,s0,"));
    }

    @Test
    void shouldDecodeAddi() throws IOException {
        assertEquals(new Instruction.Addi(18, 8, 1), Decoder.decode("ADDI,s2,s0,1"));
        assertThrows(IllegalArgumentException.class, () -> Decoder.decode("ADDI,s2,s0"));
        assertThrows(IllegalArgumentException.class, () -> Decoder.decode("ADDI,s2,s0,"));
        assertThrows(IllegalArgumentException.class, () -> Decoder.decode("ADD,s2,,1"));
    }

    @Test
    void shouldDecodeLw() throws IOException {
        assertEquals(new Instruction.Lw(18, 8, 1), Decoder.decode("LW,s2,s0,1"));
        assertThrows(IllegalArgumentException.class, () -> Decoder.decode("LW,s2,s0"));
        assertThrows(IllegalArgumentException.class, () -> Decoder.decode("LW,s2,s0,"));
        assertThrows(IllegalArgumentException.class, () -> Decoder.decode("LW,s2,,1"));
    }

    @Test
    void shouldDecodeSw() throws IOException {
        assertEquals(new Instruction.Sw(9, 8, 1), Decoder.decode("SW,s1,s0,1"));
        assertThrows(IllegalArgumentException.class, () -> Decoder.decode("SW,s1,s0"));
        assertThrows(IllegalArgumentException.class, () -> Decoder.decode("SW,s1,s0,"));
        assertThrows(IllegalArgumentException.class, () -> Decoder.decode("SW,s1,,1"));
    }

    @Test
    void shouldDecodeSub() throws IOException {
        assertEquals(new Instruction.Sub(18, 8, 9), Decoder.decode("SUB,s2,s0,s1"));
        assertThrows(IllegalArgumentException.class, () -> Decoder.decode("SUB,s2,s0"));
        assertThrows(IllegalArgumentException.class, () -> Decoder.decode("SUB,s2,s0,"));
    }

    @Test
    void shouldDecodeJal() throws IOException {
        assertEquals(new Instruction.Jal(18, 1), Decoder.decode("JAL,s2,1,"));
        assertThrows(IllegalArgumentException.class, () -> Decoder.decode("JAL,s2"));
        assertThrows(IllegalArgumentException.class, () -> Decoder.decode("JAL,s2,1"));
        assertThrows(IllegalArgumentException.class, () -> Decoder.decode("JAL,s2,"));
        assertThrows(IllegalArgumentException.class, () -> Decoder.decode("JAL,s2,,"));
        assertThrows(IllegalArgumentException.class, () -> Decoder.decode("JAL,,1,"));
    }

    @Test
    void shouldDecodeJalr() throws IOException {
        assertEquals(new Instruction.Jalr(18, 8, 1), Decoder.decode("JALR,s2,s0,1"));
        assertThrows(IllegalArgumentException.class, () -> Decoder.decode("JALR,s2,s0"));
        assertThrows(IllegalArgumentException.class, () -> Decoder.decode("JALR,s2,s0,"));
        assertThrows(IllegalArgumentException.class, () -> Decoder.decode("JALR,s2,,1"));
    }

    @Test
    void shouldDecodeBeq() throws IOException {
        assertEquals(new Instruction.Beq(8, 9, 1), Decoder.decode("BEQ,s0,s1,1"));
        assertThrows(IllegalArgumentException.class, () -> Decoder.decode("BEQ,s0,s1"));
        assertThrows(IllegalArgumentException.class, () -> Decoder.decode("BEQ,s0,s1,"));
        assertThrows(IllegalArgumentException.class, () -> Decoder.decode("BEQ,s0,,1"));
    }

    @Test
    void shouldDecodeBne() throws IOException {
        assertEquals(new Instruction.Bne(8, 9, 1), Decoder.decode("BNE,s0,s1,1"));
        assertThrows(IllegalArgumentException.class, () -> Decoder.decode("BNE,s0,s1"));
        assertThrows(IllegalArgumentException.class, () -> Decoder.decode("BNE,s0,s1,"));
        assertThrows(IllegalArgumentException.class, () -> Decoder.decode("BNE,s0,,1"));
    }

    @Test
    void shouldDecodeBlt() throws IOException {
        assertEquals(new Instruction.Blt(8, 9, 1), Decoder.decode("BLT,s0,s1,1"));
        assertThrows(IllegalArgumentException.class, () -> Decoder.decode("BLT,s0,s1"));
        assertThrows(IllegalArgumentException.class, () -> Decoder.decode("BLT,s0,s1,"));
        assertThrows(IllegalArgumentException.class, () -> Decoder.decode("BLT,s0,,1"));
    }

    @Test
    void shouldDecodeBge() throws IOException {
        assertEquals(new Instruction.Bge(8, 9, 1), Decoder.decode("BGE,s0,s1,1"));
        assertThrows(IllegalArgumentException.class, () -> Decoder.decode("BGE,s0,s1"));
        assertThrows(IllegalArgumentException.class, () -> Decoder.decode("BGE,s0,s1,"));
        assertThrows(IllegalArgumentException.class, () -> Decoder.decode("BGE,s0,,1"));
    }
}
