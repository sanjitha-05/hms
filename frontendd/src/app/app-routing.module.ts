import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { PatientComponent } from './patient/patient.component';
import { BookAppointmentComponent } from './book-appointment/book-appointment.component';
import { MedicalHistoryComponent } from './medical-history/medical-history.component';
import { DoctorComponent } from './doctor/doctor.component';
import { NotificationComponent } from './notification/notifications.component';
import { MyAppointmentsComponent } from './my-appointments/my-appointments.component';
import { ProfileComponent } from './profile/profile.component';
import { DoctorAppointmentsComponent } from './doctor-appointments/doctor-appointments.component';
import { ManageAvailabilityComponent } from './manage-availability/manage-availability.component';

const routes: Routes = [
  { path: '', component: HomeComponent }, // Default route
  { path: 'login', component: LoginComponent }, // Placeholder
  { path: 'register', component: RegisterComponent }, // Placeholder
  { path: 'patient', component: PatientComponent },
  { path: 'book-appointment', component: BookAppointmentComponent },
  { path: 'medical-history', component: MedicalHistoryComponent },
  { path: 'doctor', component: DoctorComponent },
  { path: 'notifications', component: NotificationComponent },
  { path: 'my-appointments', component: MyAppointmentsComponent },
  { path: 'profile', component: ProfileComponent },
  { path: 'doctor-appointments', component: DoctorAppointmentsComponent },
  { path: 'doctor-notifications', component: NotificationComponent },
  { path: 'manage-availability', component: ManageAvailabilityComponent },

  // { path: '', redirectTo: '/patient', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
