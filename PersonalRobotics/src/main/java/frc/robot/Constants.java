package frc.robot;
import edu.wpi.first.units.Units;
import edu.wpi.first.units.measure.Current;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
  public static final double EPSILON = 0.000001; // lgtm

  public static class OperatorConstants {
    public static final int kDriverControllerPort = 0;
  }

  public static class CoralArmHardware {
    public static final int ARM_MOTOR_ID = 52;
    public static final int EFFECTOR_MOTOR_ID = 51;
    public static final double ALLOWED_CLOSED_LOOP_ERROR = 0.05;
    public static final Current ARM_MOTOR_CURRENT_LIMIT = Units.Amps.of(40);
    public static final Current ROLLER_MOTOR_CURRENT_LIMIT = Units.Amps.of(40);
    public static final Current ARM_STALL_CURRENT = Units.Amps.of(20);
    public static final Current ROLLER_STALL_CURRENT = Units.Amps.of(30);
  }
  public static class Swerve {
    public static final double MAX_SPEED = 4.4196; // 14.5 feet to meters
    public static final double DEADBAND = 0.08;
    public static final double TRANSLATION_SCALE = 0.8;
    public static final double GIMP_SCALE = 0.45;
    public static final double AUTO_DRIVE_TIME = 2;
    public static final double AUTO_FORWARD_SPEED = -0.15;
  }
}
