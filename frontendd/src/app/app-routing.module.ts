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

const routes: Routes = [
  { path: '', component: HomeComponent }, // Default route
  { path: 'login', component: LoginComponent }, // Placeholder
  { path: 'register', component: RegisterComponent }, // Placeholder
  { path: 'patient', component: PatientComponent },
  { path: 'book-appointment', component: BookAppointmentComponent },
  { path: 'medical-history', component: MedicalHistoryComponent },
  { path: 'doctor', component: DoctorComponent },
  { path: 'notifications', component: NotificationComponent }
  // { path: '', redirectTo: '/patient', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
