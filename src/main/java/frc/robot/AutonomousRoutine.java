package frc.robot;

/**
 * Stores an autonomous program the robot can execute.
 */
class AutonomousRoutine {

    public AutonomousRoutine() {
    }
    
    /**************** Teleop Record Methods **************/
    
    public void startRecording(String programName) {
    }
    
    public void stopRecording() {
    }
    
    /**
     * Record the state of the bot for the current time period
     */
    public void recordState() {
    }

    /*****************  Autonomous Methods ****************/
    
    
    public void startAutonomous(String programName) {
    }
    
    public void cancelAutonomous() {
    }
    
    public boolean isComplete() {
        return true;
    }
    
    public double getLeftMotorSpeed() {
        return 0.0;
    }
    
    public double getRightMotorSpeed() {
        return 0.0;
    }
    
    public double getBallCollectionArmLiftSpeed() {
        return 0.0;
    }
    
    public double getBallCollectionIntakeRotorSpeed() {
        return 0.0;
    }
}
