package com.pjtsearch.branchprediction;

public final class Instruction {
    public static sealed interface SumType permits Add, Addi, Lw, Sw, Sub, Jal, Jalr, Beq, Bne, Blt, Bge, Out, Nop {
        String getName();
    }

    public static final record Add(int rd, int rs1, int rs2)
            implements SumType {
        @Override
        public String getName() {
            return "Add";
        }
    };

    public static final record Addi(int rd, int rs1, int imm) implements SumType {
        @Override
        public String getName() {
            return "Addi";
        }
    };

    public static final record Lw(int rd, int rs1, int offset) implements SumType {
        @Override
        public String getName() {
            return "Lw";
        }
    };

    public static final record Sw(int rs2, int rs1, int offset)
            implements SumType {
        @Override
        public String getName() {
            return "Sw";
        }
    };

    public static final record Sub(int rd, int rs1, int rs2)
            implements SumType {
        @Override
        public String getName() {
            return "Sub";
        }
    };

    public static final record Jal(int rd, int offset) implements SumType {
        @Override
        public String getName() {
            return "Jal";
        }
    };

    public static final record Jalr(int rd, int rs1, int offset)
            implements SumType {
        @Override
        public String getName() {
            return "Jalr";
        }
    };

    public static final record Beq(int rs1, int rs2, int offset)
            implements SumType {
        @Override
        public String getName() {
            return "Beq";
        }
    };

    public static final record Bne(int rs1, int rs2, int offset)
            implements SumType {
        @Override
        public String getName() {
            return "Bne";
        }
    };

    public static final record Blt(int rs1, int rs2, int offset)
            implements SumType {
        @Override
        public String getName() {
            return "Blt";
        }
    };

    public static final record Bge(int rs1, int rs2, int offset)
            implements SumType {
        @Override
        public String getName() {
            return "Bge";
        }
    }

    public static final record Out(int rs1)
            implements SumType {
        @Override
        public String getName() {
            return "Out";
        }
    }

    public static final record Nop() implements SumType {
        @Override
        public String getName() {
            return "Nop";
        }
    }

}
