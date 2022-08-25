import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
import ru.netology.patient.entity.BloodPressure;
import ru.netology.patient.entity.HealthInfo;
import ru.netology.patient.entity.PatientInfo;
import ru.netology.patient.repository.PatientInfoRepository;
import ru.netology.patient.service.alert.SendAlertService;
import ru.netology.patient.service.medical.MedicalServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MedicalServiceImplTest {

    @ParameterizedTest
    @CsvSource(value = {
            "140| 90| 1| Warning, patient with id: 001, need help",
            "140| 95| 0| ",
    }, delimiterString = "|")
    public void checkBloodPressureTest(int high, int low, int numInvoked, String message) {
        PatientInfo patient = new PatientInfo("001", "Panam", "Palmer",
                LocalDate.of(1990, 12, 11),
                new HealthInfo(BigDecimal.valueOf(36.6), new BloodPressure(120, 75)));
        PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoRepository.class);
        Mockito.when(patientInfoRepository.getById("001"))
                .thenReturn(patient);
        SendAlertService sendAlertService = Mockito.mock(SendAlertService.class);
        Mockito.doNothing().when(sendAlertService).send(Mockito.anyString());
        MedicalServiceImpl medicalService = new MedicalServiceImpl(patientInfoRepository, sendAlertService);
        medicalService.checkBloodPressure("001", new BloodPressure(high, low));
        Mockito.verify(sendAlertService, Mockito.times(numInvoked))
                .send(message);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "38.6| 1| Warning, patient with id: 001, need help",
            "38.6| 0| ",
    }, delimiterString = "|")
    public void checkTemperatureTest(BigDecimal temperature, int numInvoked, String message) {
        PatientInfo patient = new PatientInfo("001", "Panam", "Palmer",
                LocalDate.of(1990, 12, 11),
                new HealthInfo(BigDecimal.valueOf(36.6), new BloodPressure(120, 75)));
        PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoRepository.class);
        Mockito.when(patientInfoRepository.getById("001"))
                .thenReturn(patient);
        SendAlertService sendAlertService = Mockito.mock(SendAlertService.class);
        Mockito.doNothing().when(sendAlertService).send(Mockito.anyString());
        MedicalServiceImpl medicalService = new MedicalServiceImpl(patientInfoRepository, sendAlertService);
        medicalService.checkTemperature("001", temperature);
        Mockito.verify(sendAlertService, Mockito.times(numInvoked))
                .send(message);
    }

    @Test
    public void getPatientInfoTest() {
        PatientInfo expected = new PatientInfo("001", "Panam", "Palmer",
                LocalDate.of(1990, 12, 11),
                new HealthInfo(BigDecimal.valueOf(36.6), new BloodPressure(120, 75)));
        PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoRepository.class);
        Mockito.when(patientInfoRepository.getById("001"))
                .thenReturn(expected);
        PatientInfo actual = patientInfoRepository.getById("001");
        Assertions.assertEquals(actual, expected);
    }
}
