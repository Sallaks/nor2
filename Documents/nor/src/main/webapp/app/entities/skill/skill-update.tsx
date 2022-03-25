import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { ISeniorityLevel } from 'app/shared/model/seniority-level.model';
import { getEntities as getSeniorityLevels } from 'app/entities/seniority-level/seniority-level.reducer';
import { getEntity, updateEntity, createEntity, reset } from './skill.reducer';
import { ISkill } from 'app/shared/model/skill.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const SkillUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const seniorityLevels = useAppSelector(state => state.seniorityLevel.entities);
  const skillEntity = useAppSelector(state => state.skill.entity);
  const loading = useAppSelector(state => state.skill.loading);
  const updating = useAppSelector(state => state.skill.updating);
  const updateSuccess = useAppSelector(state => state.skill.updateSuccess);
  const handleClose = () => {
    props.history.push('/skill');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getSeniorityLevels({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...skillEntity,
      ...values,
      seniorityLevel: seniorityLevels.find(it => it.id.toString() === values.seniorityLevel.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...skillEntity,
          seniorityLevel: skillEntity?.seniorityLevel?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="norApp.skill.home.createOrEditLabel" data-cy="SkillCreateUpdateHeading">
            <Translate contentKey="norApp.skill.home.createOrEditLabel">Create or edit a Skill</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="skill-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('norApp.skill.name')}
                id="skill-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                id="skill-seniorityLevel"
                name="seniorityLevel"
                data-cy="seniorityLevel"
                label={translate('norApp.skill.seniorityLevel')}
                type="select"
              >
                <option value="" key="0" />
                {seniorityLevels
                  ? seniorityLevels.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/skill" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default SkillUpdate;
