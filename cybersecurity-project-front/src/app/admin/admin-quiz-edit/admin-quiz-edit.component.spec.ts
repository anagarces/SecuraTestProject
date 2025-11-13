import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminQuizEditComponent } from './admin-quiz-edit.component';

describe('AdminQuizEditComponent', () => {
  let component: AdminQuizEditComponent;
  let fixture: ComponentFixture<AdminQuizEditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AdminQuizEditComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminQuizEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
