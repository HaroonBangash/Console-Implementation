"use client";

import * as React from "react";
import {
  CartesianGrid,
  Line,
  LineChart,
  ResponsiveContainer,
  Tooltip,
  XAxis,
  YAxis
} from "recharts";

import { PageHeader } from "@/components/page-header";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { analyticsOverview } from "@/mock-api/analytics";

export default function AnalyticsPage() {
  return (
    <div className="space-y-6">
      <PageHeader
        title="Analytics overview"
        description="Real-time insight into learner engagement and employment outcomes."
      />
      <div className="grid gap-6 md:grid-cols-3">
        <Card>
          <CardHeader>
            <CardTitle>Active learners</CardTitle>
            <CardDescription>Currently participating in live programmes.</CardDescription>
          </CardHeader>
          <CardContent className="text-3xl font-semibold">{analyticsOverview.activeLearners}</CardContent>
        </Card>
        <Card>
          <CardHeader>
            <CardTitle>Avg. quiz score</CardTitle>
            <CardDescription>Across latest cohort submissions.</CardDescription>
          </CardHeader>
          <CardContent className="text-3xl font-semibold">{analyticsOverview.averageQuizScore}%</CardContent>
        </Card>
        <Card>
          <CardHeader>
            <CardTitle>Placement rate</CardTitle>
            <CardDescription>Within 3 months of completion.</CardDescription>
          </CardHeader>
          <CardContent className="text-3xl font-semibold">{analyticsOverview.placementRate}%</CardContent>
        </Card>
      </div>
      <div className="grid gap-6 lg:grid-cols-2">
        <Card>
          <CardHeader>
            <CardTitle>Learner growth</CardTitle>
            <CardDescription>Active learners per month</CardDescription>
          </CardHeader>
          <CardContent className="h-64">
            <ResponsiveContainer width="100%" height="100%">
              <LineChart data={analyticsOverview.learnerTrend}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="month" />
                <YAxis />
                <Tooltip />
                <Line type="monotone" dataKey="value" stroke="#2563eb" strokeWidth={2} dot={false} />
              </LineChart>
            </ResponsiveContainer>
          </CardContent>
        </Card>
        <Card>
          <CardHeader>
            <CardTitle>Placement rate trend</CardTitle>
            <CardDescription>Percentage of learners placed</CardDescription>
          </CardHeader>
          <CardContent className="h-64">
            <ResponsiveContainer width="100%" height="100%">
              <LineChart data={analyticsOverview.placementTrend}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="month" />
                <YAxis domain={[0, 100]} />
                <Tooltip />
                <Line type="monotone" dataKey="value" stroke="#10b981" strokeWidth={2} dot={false} />
              </LineChart>
            </ResponsiveContainer>
          </CardContent>
        </Card>
      </div>
    </div>
  );
}
