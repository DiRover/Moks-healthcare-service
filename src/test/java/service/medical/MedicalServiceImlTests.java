package service.medical;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import ru.netology.patient.entity.BloodPressure;
import ru.netology.patient.entity.HealthInfo;
import ru.netology.patient.entity.PatientInfo;
import ru.netology.patient.repository.PatientInfoFileRepository;
import ru.netology.patient.service.alert.SendAlertService;
import ru.netology.patient.service.medical.MedicalServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class MedicalServiceImlTests {
    PatientInfo patientInfo;
    BloodPressure bloodPressure;
    BigDecimal temperature;
    BigDecimal normalTemperature;

    @BeforeEach
    public void setPatientInfo() {
        patientInfo = new PatientInfo("1234", "Семен", "Михайлов", LocalDate.of(1982, 1, 16),
                new HealthInfo(new BigDecimal("36.6"), new BloodPressure(125, 78)));

        bloodPressure = new BloodPressure(60, 120);

        temperature = new BigDecimal("34.1");
        normalTemperature = new BigDecimal("36.6");
    }

    @AfterEach
    public void tearDown() {
        patientInfo = null;
        bloodPressure = null;
        temperature = null;
    }

    @Test
    public void test_check_bad_blood_pressure() {

        PatientInfoFileRepository patientInfoFileRepository = Mockito.mock(PatientInfoFileRepository.class);
        SendAlertService sendAlertService = Mockito.mock(SendAlertService.class);

        String id = patientInfo.getId();

        Mockito.when(patientInfoFileRepository.getById(id)).thenReturn(patientInfo);

        String expected = String.format("Warning, patient with id: %s, need help", id);

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

        MedicalServiceImpl medicalService = new MedicalServiceImpl(patientInfoFileRepository, sendAlertService);

        medicalService.checkBloodPressure(id, bloodPressure);

        Mockito.verify(sendAlertService).send(argumentCaptor.capture());

        assertThat(argumentCaptor.getValue(), is(expected));
    }

    @Test
    public void test_check_bad_temperature() {

        PatientInfoFileRepository patientInfoFileRepository = Mockito.mock(PatientInfoFileRepository.class);
        SendAlertService sendAlertService = Mockito.mock(SendAlertService.class);

        String id = patientInfo.getId();

        Mockito.when(patientInfoFileRepository.getById(id)).thenReturn(patientInfo);

        String expected = String.format("Warning, patient with id: %s, need help", id);

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

        MedicalServiceImpl medicalService = new MedicalServiceImpl(patientInfoFileRepository, sendAlertService);

        medicalService.checkTemperature(id, temperature);

        Mockito.verify(sendAlertService).send(argumentCaptor.capture());

        assertThat(argumentCaptor.getValue(), is(expected));
    }

    @Test
    public void test_check_normal_temperature() {

        PatientInfoFileRepository patientInfoFileRepository = Mockito.mock(PatientInfoFileRepository.class);
        SendAlertService sendAlertService = Mockito.mock(SendAlertService.class);

        String id = patientInfo.getId();

        Mockito.when(patientInfoFileRepository.getById(id)).thenReturn(patientInfo);

        MedicalServiceImpl medicalService = new MedicalServiceImpl(patientInfoFileRepository, sendAlertService);

        medicalService.checkTemperature(id, normalTemperature);

        Mockito.verify(sendAlertService, Mockito.never()).send(Mockito.anyString());
    }
}
