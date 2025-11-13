import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminQuizCreateComponent } from './admin-quiz-create.component';

describe('AdminQuizCreateComponent', () => {
  let component: AdminQuizCreateComponent;
  let fixture: ComponentFixture<AdminQuizCreateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AdminQuizCreateComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminQuizCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
