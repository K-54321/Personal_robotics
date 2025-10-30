package frc.robot.Subsystems;

import static edu.wpi.first.units.Units.Percent;
import static edu.wpi.first.units.Units.Value;

import java.util.function.BooleanSupplier;

import org.lasarobotics.fsm.StateMachine;
import org.lasarobotics.fsm.SystemState;
import org.littletonrobotics.junction.Logger;

import com.revrobotics.spark.ClosedLoopSlot;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkClosedLoopController.ArbFFUnits;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.units.Units;
import edu.wpi.first.units.measure.Dimensionless;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.Constants;

public class CoralSubsystem extends StateMachine implements AutoCloseable {

    public static record Hardware (
        SparkMax coralMotor
    ) {}

    static final Dimensionless INTAKE_MOTOR_SPEED = Percent.of(100);
    
    public enum CoralSubsystemStates implements SystemState {
        NOTHING {
            @Override
            public SystemState nextState() {
                return this;
            }
        },
        REST {

            @Override
            public void initialize() {
                getInstance().stopMotor();
            }

            @Override
            public void execute() {
            }

            @Override
            public SystemState nextState() {
                if (getInstance().m_intakeCoralButton.getAsBoolean()) return INTAKE;

                return this;
            }
        },
        INTAKE {
            static Timer m_intaketimer = new Timer();

            @Override
            public void initialize() {
                getInstance().setMotorToSpeed(INTAKE_MOTOR_SPEED);

                m_intaketimer.reset();
                m_intaketimer.start();
            }
            @Override
            public void execute(){
                getInstance().runIntake();   
            }
            @Override
            public SystemState nextState() {
                if (getInstance().m_intakeCoralButton.getAsBoolean()) return INTAKE;

                return REST;
            }
        
    }
    }
    
    private static CoralSubsystem s_coralSubsystemInstance;
    private final SparkMax m_coralMotor;
    private final SparkMaxConfig m_coralMotorConfig;
    private BooleanSupplier m_intakeCoralButton;
    public static CoralSubsystem getInstance() {
        if (s_coralSubsystemInstance == null) {
            s_coralSubsystemInstance = new CoralSubsystem(CoralSubsystem.initializeHardware());
        }
        return s_coralSubsystemInstance;
    }

    public CoralSubsystem(Hardware hardware) {
        super(CoralSubsystemStates.INTAKE);
        m_coralMotor = hardware.coralMotor;
        m_coralMotorConfig = new SparkMaxConfig();
        m_coralMotorConfig.closedLoop.maxMotion
            .maxAcceleration(750)
            .maxVelocity(750);
        m_coralMotorConfig.smartCurrentLimit((int)Constants.CoralArmHardware.ROLLER_MOTOR_CURRENT_LIMIT.in(Units.Amps));
        m_coralMotor.configure(m_coralMotorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }
    public void configureBindings(
        
        BooleanSupplier intakeCoralButton
        
    ) {
        m_intakeCoralButton = intakeCoralButton;
        
    }


    public void stopMotor() {
        m_coralMotor.stopMotor();
    }

    public void setMotorToSpeed(Dimensionless speed) {
        m_coralMotor.getClosedLoopController().setReference(speed.in(Value), ControlType.kDutyCycle, ClosedLoopSlot.kSlot0, 0.0, ArbFFUnits.kVoltage);
    }

    public boolean intakeIsStalled() {
        return Units.Amps.of(getInstance().m_coralMotor.getOutputCurrent()).gte(Constants.CoralArmHardware.ROLLER_STALL_CURRENT);
    }

    public void runIntake(){
        m_coralMotor.set(INTAKE_MOTOR_SPEED.in(Value));
    }

    @Override
    public void periodic() {
        Logger.recordOutput(getName() + "/state", getState().toString());
        Logger.recordOutput(getName() + "/inputs/rollerCurrent", m_coralMotor.getAppliedOutput());

    }
    public static Hardware initializeHardware() {
        Hardware coralSubsystemHardware = new Hardware(
            new SparkMax(Constants.CoralArmHardware.EFFECTOR_MOTOR_ID, MotorType.kBrushless)
        );
        return coralSubsystemHardware;
    }
    @Override
    public void close() {
        m_coralMotor.close();
    }
}
