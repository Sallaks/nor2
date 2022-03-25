import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './seniority-level.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const SeniorityLevelDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const seniorityLevelEntity = useAppSelector(state => state.seniorityLevel.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="seniorityLevelDetailsHeading">
          <Translate contentKey="norApp.seniorityLevel.detail.title">SeniorityLevel</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{seniorityLevelEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="norApp.seniorityLevel.name">Name</Translate>
            </span>
          </dt>
          <dd>{seniorityLevelEntity.name}</dd>
        </dl>
        <Button tag={Link} to="/seniority-level" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/seniority-level/${seniorityLevelEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default SeniorityLevelDetail;
