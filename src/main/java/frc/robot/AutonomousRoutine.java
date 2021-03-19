package frc.robot;

/**
 * Stores an autonomous program the robot can execute.
 */
public class AutonomousRoutine {

    private static int MAX_ENTRIES = 15000;
    
    // Program storage. An entry is recorded every 20ms. Each entry is 4 bytes
    // So, 50 entries = 1 second
    //     3,000 entries = 1 minute
    //     15,000 entries = 5 minutes = 60,000 bytes ~ 58K
    int[] program = new int[MAX_ENTRIES];
    
    // Reuse the same record for packing/unpacking so we prevent
    // GC from running
    Record record = new Record();
    
    // The position in the program where the next step will be recorded or played back
    int index = 0;
    
    int programLength = 0;
        
    public AutonomousRoutine() {
    }
    
    private class Record {
        public int left;         // bits 20 .. 32 (13 bits)
        public int right;        // bits  7 .. 19 (13 bits)
        public int arm;          // bit 3,4 - 0:down, 1:going up, 2:going down, 3:up
        public int roller;       // bit 1,2 - 0:stopped, 1:in, 2:out
        
        public int pack() {
            return (left << 19) | 
                (right << 6) | 
                (arm << 2) | 
                roller;
        }
        
        public void unpack(int packed) {
            this.left = (packed >> 19) & 0x1fff;
            this.right = (packed >>  6) & 0x1fff;
            this.arm = (packed >> 2) & 0x3;
            this.roller = (packed & 0x3);
        }
    }
    
    
    /**************** Teleop Record Methods **************/
    
    public void startRecording() {
        index = 0;
        programLength = 0;
    }
    
    /**
     * Record the state of the bot for the current bot position
     */
    public void recordState(
        int leftMotorPosition, 
        int rightMotorPosition, 
        int arm, 
        int roller) {
        
        if(index < MAX_ENTRIES) {
            record.left = leftMotorPosition; 
            record.right = rightMotorPosition;
            record.arm = arm;
            record.roller = roller;

            program[index] = record.pack();

            index++;
            programLength++;
        }
    }

    /*****************  Autonomous Methods ****************/
    
    
    public void startAutonomous() {
        index = 0;
    }
    
    public void next(){
        index++;
        if(index<programLength){
            record.unpack(program[index]);
        }
    }
    
    public boolean isComplete() {
        return index < programLength;
    }
    
    public int getLeftMotor() {
        return record.left;
    }
    
    public int getRightMotor() {
        return record.right;
    }
    
    public int getBallCollectionArm() {
        return record.arm;
    }
    
    public int getBallCollectionIntakeRoller() {
        return record.roller;
    }
}
