package com.pjtsearch.branchprediction;


import java.util.List;
import java.util.Objects;

public class Executor {
    public static List<WriteIndicator.SumType> execute(Instruction.SumType instruction, RegisterFile<Integer> registerFile, Cache<Integer> dataCache, int instructionAddress) {
        String name = instruction.getName();
        switch (name) {
            case "Add" -> {
                Instruction.Add instructionAdd = (Instruction.Add) instruction;
                int operand1 = registerFile.get(instructionAdd.rs1());
                int operand2 = registerFile.get(instructionAdd.rs2());
                int destination = instructionAdd.rd();
                return List.of(new WriteIndicator.WriteRegister(destination, operand1 + operand2));
            }
            case "Addi" -> {
                Instruction.Addi instructionAddi = (Instruction.Addi) instruction;
                int operand1 =  registerFile.get(instructionAddi.rs1());
                int immediate = instructionAddi.imm();
                int destination = instructionAddi.rd();
                return List.of(new WriteIndicator.WriteRegister(destination, operand1 + immediate));
            }
            case "Lw" -> {
                Instruction.Lw instructionLw = (Instruction.Lw) instruction;
                int memoryVal = dataCache.get(registerFile.get(instructionLw.rs1()) + instructionLw.offset());
                int destination = instructionLw.rd();
                return List.of(new WriteIndicator.WriteRegister(destination, memoryVal));
            }
            case "Sw" -> {
                Instruction.Sw instructionSw = (Instruction.Sw) instruction;
                int memoryDestination = registerFile.get(instructionSw.rs1()) + instructionSw.offset();
                int registerVal = registerFile.get(instructionSw.rs2());
                return List.of(new WriteIndicator.WriteMemory(memoryDestination, registerVal));
            }
            case "Sub" -> {
                Instruction.Sub instructionSub = (Instruction.Sub) instruction;
                int operand1 =  registerFile.get(instructionSub.rs1());
                int operand2 = registerFile.get(instructionSub.rs2());
                int destination = instructionSub.rd();
                return List.of(new WriteIndicator.WriteRegister(destination, operand1 - operand2));
            }
            case "Jal"-> {
                Instruction.Jal instructionJal = (Instruction.Jal) instruction;
                int returnAddressLocation = instructionJal.rd();
                int offset = instructionJal.offset();
                int returnAddress = instructionAddress +1;
                int newPC = instructionAddress + offset;
                return List.of(new WriteIndicator.WritePC(newPC),
                        new WriteIndicator.WriteRegister(returnAddressLocation,returnAddress));
            }
            case "Jalr" -> {
                Instruction.Jalr instructionJalr = (Instruction.Jalr) instruction;
                int returnAddressLocation = instructionJalr.rd();
                int offset = instructionJalr.offset();
                int returnAddress = instructionAddress + 1;
                int instructionAddressVal = registerFile.get(instructionJalr.rs1());
                int newPC = instructionAddressVal + offset;
                return List.of(new WriteIndicator.WritePC(newPC),
                        new WriteIndicator.WriteRegister(returnAddressLocation,returnAddress));
            }
            case "Beq" -> {
                Instruction.Beq instructionBeq = (Instruction.Beq) instruction;
                boolean branch = Objects.equals(registerFile.get(instructionBeq.rs1()), registerFile.get(instructionBeq.rs2()));
                int newPC = instructionAddress + instructionBeq.offset();
                if (branch) {
                    return List.of(new WriteIndicator.WritePC(newPC));
                } else {
                    return List.of(new WriteIndicator.WritePC(instructionAddress + 1));
                }
            }
            case "Bne" -> {
                Instruction.Bne instructionBne = (Instruction.Bne) instruction;
                boolean branch = !Objects.equals(registerFile.get(instructionBne.rs1()), registerFile.get(instructionBne.rs2()));
                int newPC = instructionAddress + instructionBne.offset();
                if (branch) {
                    return List.of(new WriteIndicator.WritePC(newPC));
                } else {
                    return List.of(new WriteIndicator.WritePC(instructionAddress + 1));
                }
            }
            case "Blt" -> {
                Instruction.Blt instructionBlt = (Instruction.Blt) instruction;
                boolean branch = registerFile.get(instructionBlt.rs1()) < registerFile.get(instructionBlt.rs2());
                int newPC = instructionAddress + instructionBlt.offset();
                if (branch) {
                    return List.of(new WriteIndicator.WritePC(newPC));
                } else {
                    return List.of(new WriteIndicator.WritePC(instructionAddress + 1));
                }
            }
            case "Bge" -> {
                Instruction.Bge instructionBge = (Instruction.Bge) instruction;
                boolean branch = registerFile.get(instructionBge.rs1()) >= registerFile.get(instructionBge.rs2());
                int newPC = instructionAddress + instructionBge.offset();
                if (branch) {
                    return List.of(new WriteIndicator.WritePC(newPC));
                } else {
                    return List.of(new WriteIndicator.WritePC(instructionAddress + 1));
                }
            }
            case "Out" -> {
                Instruction.Out instructionOut = (Instruction.Out) instruction;
                int address = instructionOut.rs1();
                System.out.println("output: " + registerFile.get(address));
                return List.of(new WriteIndicator.WriteRegister(33,registerFile.get(address)));
            }
            case "Nop" -> {
                return List.of();
            }
        } 
        throw new UnsupportedOperationException();
    }
}
