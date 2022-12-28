package com.pjtsearch.branchprediction;

import java.io.IOException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.google.common.collect.ImmutableList;

import javax.annotation.Nullable;

public class Decoder {
    static Instruction.SumType decode(String raw) throws IOException {
        System.out.println("raw: " + raw);
        ImmutableList<CSVRecord> records = ImmutableList
                .copyOf(CSVParser.parse(raw, CSVFormat.DEFAULT.builder().setHeader("name", "arg1", "arg2", "arg3").build()));
        assert records.size() == 1;
        String name = records.get(0).get("name");
        String arg1 = records.get(0).get("arg1");
        String arg2 = records.get(0).get("arg2");
        String arg3 = records.get(0).get("arg3");
        return switch (name) {
            case "ADD" -> new Instruction.Add(getArgRegisterNumber(arg1),
                    getArgRegisterNumber(arg2), getArgRegisterNumber(arg3));
            case "ADDI" -> new Instruction.Addi(getArgRegisterNumber(arg1), getArgRegisterNumber(arg2),
                    Integer.parseInt(assureValidArg(arg3)));
            case "BEQ" -> new Instruction.Beq(getArgRegisterNumber(arg1), getArgRegisterNumber(arg2),
                    Integer.parseInt(assureValidArg(arg3)));
            case "BGE" -> new Instruction.Bge(getArgRegisterNumber(arg1), getArgRegisterNumber(arg2),
                    Integer.parseInt(assureValidArg(arg3)));
            case "BLT" -> new Instruction.Blt(getArgRegisterNumber(arg1), getArgRegisterNumber(arg2),
                    Integer.parseInt(assureValidArg(arg3)));
            case "BNE" -> new Instruction.Bne(getArgRegisterNumber(arg1), getArgRegisterNumber(arg2),
                    Integer.parseInt(assureValidArg(arg3)));
            case "JAL" ->
                new Instruction.Jal(getArgRegisterNumber(arg1), Integer.parseInt(assureValidArg(arg2)));
            case "JALR" -> new Instruction.Jalr(getArgRegisterNumber(arg1), getArgRegisterNumber(arg2),
                    Integer.parseInt(assureValidArg(arg3)));
            case "LW" -> new Instruction.Lw(getArgRegisterNumber(arg1), getArgRegisterNumber(arg2),
                    Integer.parseInt(assureValidArg(arg3)));
            case "SUB" -> new Instruction.Sub(getArgRegisterNumber(arg1), getArgRegisterNumber(arg2),
                    getArgRegisterNumber(arg3));
            case "SW" -> new Instruction.Sw(getArgRegisterNumber(arg1), getArgRegisterNumber(arg2),
                    Integer.parseInt(assureValidArg(arg3))); 
            case "OUT" -> new Instruction.Out(getArgRegisterNumber(arg1));
            case "NOP" -> new Instruction.Nop();
            default -> throw new IllegalArgumentException("Unexpected value: " + name);
        };
    }
    private static String assureValidArg(@Nullable String arg) {
        if (arg == null || arg.equals(""))
            throw new IllegalArgumentException();
        return arg;
    }

    private static int getArgRegisterNumber(String registerArg) {
        return getRegisterNumber(assureValidArg(registerArg));
    }

    private static int getRegisterNumber(String abiName) {
        return switch (abiName) {
            case "zero" -> 0;
            case "ra" -> 1;
            case "sp" -> 2;
            case "gp" -> 3;
            case "tp" -> 4;
            case "t0" -> 5;
            case "t1" -> 6;
            case "t2" -> 7;
            case "s0" -> 8;
            case "s1" -> 9;
            case "a0" -> 10;
            case "a1" -> 11;
            case "a2" -> 12;
            case "a3" -> 13;
            case "a4" -> 14;
            case "a5" -> 15;
            case "a6" -> 16;
            case "a7" -> 17;
            case "s2" -> 18;
            case "s3" -> 19;
            case "s4" -> 20;
            case "s5" -> 21;
            case "s6" -> 22;
            case "s7" -> 23;
            case "s8" -> 24;
            case "s9" -> 25;
            case "s10" -> 26;
            case "s11" -> 27;
            case "t3" -> 28;
            case "t4" -> 29;
            case "t5" -> 30;
            case "t6" -> 31;
            default -> throw new IllegalStateException("Invalid register name: " + abiName);
        };
    }
}
