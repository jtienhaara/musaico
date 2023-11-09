            final BlockingMustComplete.CancelledTime<VALUE> cancelled_time =
                new BlockingMustComplete.CancelledTime<VALUE> (
                    this,
                    result.elapsedTimeInSeconds () );

            final ValueViolation value_violation =
                BlockingMustComplete.violation (
                    this.result.maxTimeoutInSeconds (),
                    cancelled_time );

            final Cancelled<VALUE> cancelled =
                new Cancelled<VALUE> ( this.type,
                                       value_violation,
                                       this.result.partialValue () );

            this.result.setFinalResult ( cancelled );
